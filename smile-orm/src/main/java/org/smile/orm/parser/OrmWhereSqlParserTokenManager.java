/* Generated By:JJTree&JavaCC: Do not edit this line. OrmWhereSqlParserTokenManager.java */
package org.smile.orm.parser;
import org.smile.util.StringUtils;
import org.smile.expression.*;
import org.smile.orm.parser.InMultiParameterExpression;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

/** Token Manager. */
public class OrmWhereSqlParserTokenManager implements OrmWhereSqlParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x40L) != 0L)
            return 87;
         if ((active0 & 0x20L) != 0L)
            return 88;
         if ((active0 & 0x800L) != 0L)
         {
            jjmatchedKind = 19;
            return 54;
         }
         if ((active0 & 0x8000000L) != 0L)
            return 89;
         return -1;
      case 1:
         if ((active0 & 0x800L) != 0L)
         {
            jjmatchedKind = 19;
            jjmatchedPos = 1;
            return 90;
         }
         return -1;
      case 2:
         if ((active0 & 0x800L) != 0L)
         {
            jjmatchedKind = 19;
            jjmatchedPos = 2;
            return 90;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 37:
         return jjStopAtPos(0, 36);
      case 38:
         return jjStopAtPos(0, 31);
      case 40:
         return jjStartNfaWithStates_0(0, 27, 89);
      case 41:
         return jjStopAtPos(0, 28);
      case 42:
         return jjStopAtPos(0, 34);
      case 43:
         return jjStopAtPos(0, 32);
      case 44:
         return jjStopAtPos(0, 33);
      case 45:
         return jjStopAtPos(0, 29);
      case 47:
         return jjStopAtPos(0, 35);
      case 60:
         return jjStartNfaWithStates_0(0, 5, 88);
      case 61:
         return jjStopAtPos(0, 7);
      case 62:
         return jjStartNfaWithStates_0(0, 6, 87);
      case 63:
         return jjStopAtPos(0, 21);
      case 94:
         return jjStopAtPos(0, 37);
      case 78:
      case 110:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 124:
         return jjStopAtPos(0, 30);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 85:
      case 117:
         return jjMoveStringLiteralDfa2_0(active0, 0x800L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 76:
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x800L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 76:
      case 108:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(3, 11, 90);
         break;
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0xffffffffffffc000L, 0x7fffffffL, 0x0L
};
static final long[] jjbitVec2 = {
   0xffffffffffffffffL, 0xffffffffffffffffL, 0x3fffffffffL, 0x0L
};
static final long[] jjbitVec3 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec4 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 87;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 90:
                  if ((0x3ff400000000000L & l) != 0L)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAdd(21);
                  }
                  if (curChar == 46)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAddTwoStates(20, 21);
                  }
                  break;
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 17)
                        kind = 17;
                     jjCheckNAddStates(0, 8);
                  }
                  else if ((0x100002600L & l) != 0L)
                  {
                     if (kind > 1)
                        kind = 1;
                  }
                  else if ((0x400000800000000L & l) != 0L)
                     jjCheckNAddTwoStates(23, 25);
                  else if (curChar == 60)
                     jjCheckNAddStates(9, 12);
                  else if (curChar == 39)
                     jjCheckNAddTwoStates(42, 43);
                  else if (curChar == 34)
                     jjCheckNAddTwoStates(36, 37);
                  else if (curChar == 40)
                     jjCheckNAddTwoStates(27, 28);
                  else if (curChar == 46)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAddTwoStates(20, 21);
                  }
                  else if (curChar == 62)
                     jjCheckNAddTwoStates(5, 6);
                  else if (curChar == 33)
                     jjCheckNAddTwoStates(2, 3);
                  if (curChar == 32)
                     jjAddStates(13, 14);
                  else if (curChar == 46)
                     jjCheckNAdd(16);
                  break;
               case 88:
                  if ((0x100002600L & l) != 0L)
                     jjCheckNAddTwoStates(71, 72);
                  else if (curChar == 61)
                  {
                     if (kind > 4)
                        kind = 4;
                  }
                  else if (curChar == 62)
                  {
                     if (kind > 2)
                        kind = 2;
                  }
                  if ((0x100002600L & l) != 0L)
                     jjCheckNAddTwoStates(69, 70);
                  break;
               case 89:
                  if (curChar == 32)
                     jjCheckNAddTwoStates(27, 28);
                  else if (curChar == 63)
                     jjCheckNAddStates(15, 17);
                  break;
               case 54:
                  if ((0x3ff400000000000L & l) != 0L)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAdd(21);
                  }
                  if (curChar == 46)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAddTwoStates(20, 21);
                  }
                  break;
               case 87:
                  if ((0x100002600L & l) != 0L)
                     jjCheckNAddTwoStates(5, 6);
                  else if (curChar == 61)
                  {
                     if (kind > 3)
                        kind = 3;
                  }
                  break;
               case 1:
                  if (curChar == 33)
                     jjCheckNAddTwoStates(2, 3);
                  break;
               case 2:
                  if ((0x100002600L & l) != 0L)
                     jjCheckNAddTwoStates(2, 3);
                  break;
               case 3:
                  if (curChar == 61 && kind > 2)
                     kind = 2;
                  break;
               case 4:
                  if (curChar == 62)
                     jjCheckNAddTwoStates(5, 6);
                  break;
               case 5:
                  if ((0x100002600L & l) != 0L)
                     jjCheckNAddTwoStates(5, 6);
                  break;
               case 6:
                  if (curChar == 61 && kind > 3)
                     kind = 3;
                  break;
               case 15:
                  if (curChar == 46)
                     jjCheckNAdd(16);
                  break;
               case 16:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 18)
                     kind = 18;
                  jjCheckNAddTwoStates(16, 17);
                  break;
               case 18:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(19);
                  break;
               case 19:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 18)
                     kind = 18;
                  jjCheckNAdd(19);
                  break;
               case 20:
                  if (curChar != 46)
                     break;
                  if (kind > 19)
                     kind = 19;
                  jjCheckNAddTwoStates(20, 21);
                  break;
               case 21:
                  if ((0x3ff400000000000L & l) == 0L)
                     break;
                  if (kind > 19)
                     kind = 19;
                  jjCheckNAdd(21);
                  break;
               case 22:
                  if ((0x400000800000000L & l) != 0L)
                     jjCheckNAddTwoStates(23, 25);
                  break;
               case 23:
                  if (curChar != 46)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAddTwoStates(23, 24);
                  break;
               case 24:
                  if ((0x3ff400000000000L & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAdd(24);
                  break;
               case 25:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAdd(25);
                  break;
               case 26:
                  if (curChar == 40)
                     jjCheckNAddTwoStates(27, 28);
                  break;
               case 27:
                  if (curChar == 32)
                     jjCheckNAddTwoStates(27, 28);
                  break;
               case 28:
                  if (curChar == 63)
                     jjCheckNAddStates(15, 17);
                  break;
               case 29:
                  if (curChar == 44)
                     jjCheckNAddTwoStates(30, 28);
                  break;
               case 30:
                  if (curChar == 32)
                     jjCheckNAddTwoStates(30, 28);
                  break;
               case 31:
                  if (curChar == 41 && kind > 22)
                     kind = 22;
                  break;
               case 32:
                  if (curChar == 32)
                     jjCheckNAddStates(18, 21);
                  break;
               case 33:
                  if (curChar == 32)
                     jjCheckNAddTwoStates(33, 29);
                  break;
               case 34:
                  if (curChar == 32)
                     jjCheckNAddTwoStates(34, 31);
                  break;
               case 35:
                  if (curChar == 34)
                     jjCheckNAddTwoStates(36, 37);
                  break;
               case 36:
                  if ((0xfffffffbffffdbffL & l) != 0L)
                     jjCheckNAddTwoStates(36, 37);
                  break;
               case 37:
                  if (curChar == 34 && kind > 26)
                     kind = 26;
                  break;
               case 39:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjAddStates(22, 23);
                  break;
               case 41:
                  if (curChar == 39)
                     jjCheckNAddTwoStates(42, 43);
                  break;
               case 42:
                  if ((0xffffff7fffffdbffL & l) != 0L)
                     jjCheckNAddTwoStates(42, 43);
                  break;
               case 43:
                  if (curChar == 39 && kind > 26)
                     kind = 26;
                  break;
               case 62:
                  if (curChar == 32)
                     jjAddStates(13, 14);
                  break;
               case 63:
                  if (curChar == 32 && kind > 8)
                     kind = 8;
                  break;
               case 68:
                  if (curChar == 60)
                     jjCheckNAddStates(9, 12);
                  break;
               case 69:
                  if ((0x100002600L & l) != 0L)
                     jjCheckNAddTwoStates(69, 70);
                  break;
               case 70:
                  if (curChar == 62 && kind > 2)
                     kind = 2;
                  break;
               case 71:
                  if ((0x100002600L & l) != 0L)
                     jjCheckNAddTwoStates(71, 72);
                  break;
               case 72:
                  if (curChar == 61 && kind > 4)
                     kind = 4;
                  break;
               case 73:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  jjCheckNAddStates(0, 8);
                  break;
               case 74:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(74, 75);
                  break;
               case 76:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  jjCheckNAdd(76);
                  break;
               case 77:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(77, 15);
                  break;
               case 78:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(78, 79);
                  break;
               case 79:
                  if (curChar != 46)
                     break;
                  if (kind > 18)
                     kind = 18;
                  jjstateSet[jjnewStateCnt++] = 80;
                  break;
               case 81:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(82);
                  break;
               case 82:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 18)
                     kind = 18;
                  jjCheckNAdd(82);
                  break;
               case 83:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(83, 84);
                  break;
               case 85:
                  if ((0x280000000000L & l) != 0L)
                     jjCheckNAdd(86);
                  break;
               case 86:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 18)
                     kind = 18;
                  jjCheckNAdd(86);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 90:
                  if ((0x7fffffeaffffffeL & l) != 0L)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAdd(21);
                  }
                  if ((0x7fffffeaffffffeL & l) != 0L)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAddTwoStates(20, 21);
                  }
                  break;
               case 0:
                  if ((0x7fffffeaffffffeL & l) != 0L)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAddTwoStates(20, 21);
                  }
                  else if (curChar == 96)
                     jjCheckNAddTwoStates(39, 40);
                  if ((0x100000001000L & l) != 0L)
                     jjAddStates(24, 25);
                  else if ((0x400000004000L & l) != 0L)
                     jjAddStates(26, 27);
                  else if ((0x800000008000L & l) != 0L)
                     jjAddStates(28, 29);
                  else if ((0x200000002L & l) != 0L)
                     jjAddStates(30, 31);
                  else if ((0x20000000200L & l) != 0L)
                     jjAddStates(32, 33);
                  else if ((0x4000000040L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  else if ((0x10000000100000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 9;
                  break;
               case 54:
                  if ((0x7fffffeaffffffeL & l) != 0L)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAdd(21);
                  }
                  if ((0x7fffffeaffffffeL & l) != 0L)
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAddTwoStates(20, 21);
                  }
                  if ((0x800000008000L & l) != 0L)
                     jjCheckNAdd(53);
                  if ((0x800000008000L & l) != 0L)
                     jjCheckNAdd(53);
                  break;
               case 7:
                  if ((0x2000000020L & l) != 0L && kind > 10)
                     kind = 10;
                  break;
               case 8:
                  if ((0x20000000200000L & l) != 0L)
                     jjCheckNAdd(7);
                  break;
               case 9:
                  if ((0x4000000040000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 10:
                  if ((0x10000000100000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 9;
                  break;
               case 11:
                  if ((0x8000000080000L & l) != 0L)
                     jjCheckNAdd(7);
                  break;
               case 12:
                  if ((0x100000001000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 13:
                  if ((0x200000002L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 14:
                  if ((0x4000000040L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  break;
               case 17:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(34, 35);
                  break;
               case 20:
                  if ((0x7fffffeaffffffeL & l) == 0L)
                     break;
                  if (kind > 19)
                     kind = 19;
                  jjCheckNAddTwoStates(20, 21);
                  break;
               case 21:
                  if ((0x7fffffeaffffffeL & l) == 0L)
                     break;
                  if (kind > 19)
                     kind = 19;
                  jjCheckNAdd(21);
                  break;
               case 23:
                  if ((0x7fffffeaffffffeL & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAddTwoStates(23, 24);
                  break;
               case 24:
                  if ((0x7fffffeaffffffeL & l) == 0L)
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAdd(24);
                  break;
               case 36:
                  jjAddStates(36, 37);
                  break;
               case 38:
                  if (curChar == 96)
                     jjCheckNAddTwoStates(39, 40);
                  break;
               case 39:
                  if ((0xfffffffeffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(39, 40);
                  break;
               case 40:
                  if (curChar == 96 && kind > 26)
                     kind = 26;
                  break;
               case 42:
                  jjAddStates(38, 39);
                  break;
               case 44:
                  if ((0x20000000200L & l) != 0L)
                     jjAddStates(32, 33);
                  break;
               case 45:
                  if ((0x400000004000L & l) != 0L && kind > 15)
                     kind = 15;
                  break;
               case 46:
                  if ((0x200000002L & l) != 0L)
                     jjAddStates(30, 31);
                  break;
               case 47:
                  if ((0x1000000010L & l) != 0L && kind > 14)
                     kind = 14;
                  break;
               case 48:
               case 49:
                  if ((0x400000004000L & l) != 0L)
                     jjCheckNAdd(47);
                  break;
               case 50:
                  if ((0x800000008000L & l) != 0L)
                     jjAddStates(28, 29);
                  break;
               case 51:
                  if ((0x4000000040000L & l) != 0L && kind > 13)
                     kind = 13;
                  break;
               case 52:
                  if ((0x400000004000L & l) != 0L)
                     jjAddStates(26, 27);
                  break;
               case 53:
                  if ((0x10000000100000L & l) != 0L && kind > 12)
                     kind = 12;
                  break;
               case 55:
                  if ((0x800000008000L & l) != 0L)
                     jjCheckNAdd(53);
                  break;
               case 56:
                  if ((0x100000001000L & l) != 0L)
                     jjAddStates(24, 25);
                  break;
               case 57:
                  if ((0x2000000020L & l) != 0L && kind > 9)
                     kind = 9;
                  break;
               case 58:
               case 60:
                  if ((0x80000000800L & l) != 0L)
                     jjCheckNAdd(57);
                  break;
               case 59:
                  if ((0x20000000200L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 58;
                  break;
               case 61:
                  if ((0x20000000200L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 60;
                  break;
               case 64:
               case 66:
                  if ((0x8000000080000L & l) != 0L)
                     jjCheckNAdd(63);
                  break;
               case 65:
                  if ((0x20000000200L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 64;
                  break;
               case 67:
                  if ((0x20000000200L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 66;
                  break;
               case 75:
                  if ((0x100000001000L & l) != 0L && kind > 16)
                     kind = 16;
                  break;
               case 80:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(40, 41);
                  break;
               case 84:
                  if ((0x2000000020L & l) != 0L)
                     jjAddStates(42, 43);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 90:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAddTwoStates(20, 21);
                  }
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAdd(21);
                  }
                  break;
               case 0:
               case 20:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 19)
                     kind = 19;
                  jjCheckNAddTwoStates(20, 21);
                  break;
               case 54:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAddTwoStates(20, 21);
                  }
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                  {
                     if (kind > 19)
                        kind = 19;
                     jjCheckNAdd(21);
                  }
                  break;
               case 21:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 19)
                     kind = 19;
                  jjCheckNAdd(21);
                  break;
               case 23:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAddTwoStates(23, 24);
                  break;
               case 24:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 20)
                     kind = 20;
                  jjCheckNAdd(24);
                  break;
               case 36:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjAddStates(36, 37);
                  break;
               case 39:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjAddStates(22, 23);
                  break;
               case 42:
                  if (jjCanMove_1(hiByte, i1, i2, l1, l2))
                     jjAddStates(38, 39);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 87 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   74, 75, 76, 77, 15, 78, 79, 83, 84, 69, 70, 71, 72, 65, 67, 29, 
   31, 32, 33, 29, 34, 31, 39, 40, 59, 61, 54, 55, 51, 51, 48, 49, 
   45, 45, 18, 19, 36, 37, 42, 43, 81, 82, 85, 86, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 159:
         return ((jjbitVec2[i2] & l2) != 0L);
      default :
         if ((jjbitVec0[i1] & l1) != 0L)
            return true;
         return false;
   }
}
private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec4[i2] & l2) != 0L);
      default :
         if ((jjbitVec3[i1] & l1) != 0L)
            return true;
         return false;
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, "\74", "\76", "\75", null, null, null, null, null, 
null, null, null, null, null, null, null, null, "\77", null, null, null, null, null, 
"\50", "\51", "\55", "\174", "\46", "\53", "\54", "\52", "\57", "\45", "\136", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x3ffc7ffffdL, 
};
static final long[] jjtoSkip = {
   0x2L, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[87];
private final int[] jjstateSet = new int[174];
protected char curChar;
/** Constructor. */
public OrmWhereSqlParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public OrmWhereSqlParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 87; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
