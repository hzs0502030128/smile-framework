package org.smile.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;

import org.smile.commons.Strings;
import org.smile.io.ByteArrayOutputStream;
import org.smile.io.IOUtils;
import org.smile.mail.att.ByteArrayAttachment;

/**
 * Received email.
 */
public class ReceivedEmail extends CommonEmail {

	public ReceivedEmail(Message message) {
		try {
			parseMessage(message);
		} catch (Exception ex) {
			throw new MailException("Message parsing failed", ex);
		}
	}

	/**
	 * Parse java <code>Message</code> and extracts all data for the received message.
	 */
	@SuppressWarnings("unchecked")
	protected void parseMessage(Message msg) throws MessagingException, IOException {
		// flags
		setFlags(msg.getFlags());

		// msg no
		setMessageNumber(msg.getMessageNumber());

		// single from
		Address[] addresses = msg.getFrom();

		if (addresses != null && addresses.length > 0) {
			setFrom(new MailAddress(addresses[0]));
		}

		// common field
		setTo(MailAddress.createFrom(msg.getRecipients(Message.RecipientType.TO)));
		setCc(MailAddress.createFrom(msg.getRecipients(Message.RecipientType.CC)));
		setBcc(MailAddress.createFrom(msg.getRecipients(Message.RecipientType.BCC)));

		setSubject(msg.getSubject());

		Date recvDate = msg.getReceivedDate();
		if (recvDate == null) {
			recvDate = new Date();
		}
		setReceiveDate(recvDate);
		setSentDate(msg.getSentDate());

		// copy headers
		Enumeration<Header> headers = msg.getAllHeaders();
		while (headers.hasMoreElements()) {
			Header header = headers.nextElement();
			setHeader(header.getName(), header.getValue());
		}

		// content
		processPart(this, msg);
	}

	/**
	 * Process single part of received message. All parts are simple added to the message, i.e. hierarchy is not saved.
	 */
	protected void processPart(ReceivedEmail email, Part part) throws IOException, MessagingException {
		Object content = part.getContent();

		if (content instanceof String) {
			String stringContent = (String) content;

			String disposition = part.getDisposition();
			if (disposition != null && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
				String contentType = part.getContentType();

				String mimeType = EmailUtil.extractMimeType(contentType);
				String encoding = EmailUtil.extractEncoding(contentType);
				String fileName = part.getFileName();
				String contentId = (part instanceof MimePart) ? ((MimePart)part).getContentID() : null;

				if (encoding == null) {
					encoding = Strings.US_ASCII;
				}

				email.addAttachment(fileName, mimeType, contentId, stringContent.getBytes(encoding));
			} else {
				String contentType = part.getContentType();
				String encoding = EmailUtil.extractEncoding(contentType);
				String mimeType = EmailUtil.extractMimeType(contentType);

				if (encoding == null) {
					encoding = Strings.US_ASCII;
				}

				email.addMessage(stringContent, mimeType, encoding);
			}
		} else if (content instanceof Multipart) {
			Multipart mp = (Multipart) content;
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				Part innerPart = mp.getBodyPart(i);
				processPart(email, innerPart);
			}
		} else if (content instanceof InputStream) {
			String fileName = part.getFileName();
			String contentId = (part instanceof MimePart) ? ((MimePart)part).getContentID() : null;
			String mimeType = EmailUtil.extractMimeType(part.getContentType());
			InputStream is = (InputStream) content;
			ByteArrayOutputStream fbaos = new ByteArrayOutputStream();
			IOUtils.copy(is, fbaos);
			email.addAttachment(fileName, mimeType, contentId, fbaos.toByteArray());
		} else if (content instanceof MimeMessage) {
			MimeMessage mimeMessage = (MimeMessage) content;

			addAttachmentMessage(new ReceivedEmail(mimeMessage));
		}
	}

	// ---------------------------------------------------------------- flags

	protected Flags flags;

	public Flags getFlags() {
		return flags;
	}

	public void setFlags(Flags flags) {
		this.flags = flags;
	}

	/**
	 * Returns <code>true</code> if message is answered.
	 */
	public boolean isAnswered() {
		return flags.contains(Flags.Flag.ANSWERED);
	}

	/**
	 * Returns <code>true</code> if message is deleted.
	 */
	public boolean isDeleted() {
		return flags.contains(Flags.Flag.DELETED);
	}

	/**
	 * Returns <code>true</code> if message is draft.
	 */
	public boolean isDraf() {
		return flags.contains(Flags.Flag.DRAFT);
	}

	/**
	 * Returns <code>true</code> is message is flagged.
	 */
	public boolean isFlagged() {
		return flags.contains(Flags.Flag.FLAGGED);
	}

	/**
	 * Returns <code>true</code> if message is recent.
	 */
	public boolean isRecent() {
		return flags.contains(Flags.Flag.RECENT);
	}

	/**
	 * Returns <code>true</code> if message is seen.
	 */
	public boolean isSeen() {
		return flags.contains(Flags.Flag.SEEN);
	}

	// ---------------------------------------------------------------- additional properties

	protected int messageNumber;
	protected Date recvDate;

	/**
	 * Returns message number.
	 */
	public int getMessageNumber() {
		return messageNumber;
	}

	/**
	 * Sets message number.
	 */
	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}

	/**
	 * Sets e-mails receive date.
	 */
	public void setReceiveDate(Date date) {
		recvDate = date;
	}

	/**
	 * Returns emails received date.
	 */
	public Date getReceiveDate() {
		return recvDate;
	}

	// ---------------------------------------------------------------- attachments

	protected List<EmailAttachment> attachments;

	/**
	 * Adds received attachment.
	 */
	public void addAttachment(String filename, String mimeType, String contentId, byte[] content) {
		if (attachments == null) {
			attachments = new ArrayList<EmailAttachment>();
		}
		EmailAttachment emailAttachment = new ByteArrayAttachment(content, mimeType, filename, contentId);
		emailAttachment.setSize(content.length);
		attachments.add(emailAttachment);
	}

	/**
	 * Returns the list of all attachments.
	 * If no attachment is available, returns <code>null</code>.
	 */
	public List<EmailAttachment> getAttachments() {
		return attachments;
	}


	// ---------------------------------------------------------------- inner messages

	protected List<ReceivedEmail> attachedMessages;

	/**
	 * Adds attached messages.
	 */
	public void addAttachmentMessage(ReceivedEmail receivedEmail) {
		if (attachedMessages == null) {
			attachedMessages = new ArrayList<ReceivedEmail>();
		}
		attachedMessages.add(receivedEmail);
	}

	/**
	 * Returns the list of attached messages.
	 * If not attached message is available, returns <code>null</code>.
	 */
	public List<ReceivedEmail> getAttachedMessages() {
		return attachedMessages;
	}

}