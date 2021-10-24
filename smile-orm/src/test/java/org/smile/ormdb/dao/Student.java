package org.smile.ormdb.dao;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.smile.db.jdbc.EnableRecordDao;
import org.smile.orm.ann.*;
import org.smile.orm.record.EnableOrmRecord;
import org.smile.orm.record.RecordDaoImpl;
@Entity(table = "student")
@HumpColumns
public class Student extends EnableOrmRecord implements ClassName{
	
	public static final EnableRecordDao<Student> dao=new RecordDaoImpl<Student>(Student.class);
	@Id
	private Long id;
	@Property
	private String name;
	@Property
	private int age;
	@Property
	private String firstName;
	private String secondName;
	@Column
	private String address;
	private Date birthday;
	@Property
	@EnableFlag
	private boolean enable;
	@Component
	private UpdateInfo updateInfo;
	
	private String className;
	@Column
	private Long classId;

	@TenantId
	@Getter
	@Setter
	private String tenantId;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public UpdateInfo getUpdateInfo() {
		return updateInfo;
	}
	public void setUpdateInfo(UpdateInfo updateInfo) {
		this.updateInfo = updateInfo;
	}
	@Override
	public void setClassName(String className) {
		this.className=className;
	}
	@Override
	public String getClassName() {
		return className;
	}
	public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
	}

	
}
