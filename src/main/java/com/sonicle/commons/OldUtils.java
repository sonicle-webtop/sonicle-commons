/*
 * sonicle-commons is a helper library developed by Sonicle S.r.l.
 * Copyright (C) 2014 Sonicle S.r.l.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY SONICLE, SONICLE DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle@sonicle.com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2014 Sonicle S.r.l.".
 */
package com.sonicle.commons;

import java.io.*;
import java.util.*;

/**
 *
 * @author gabriele.bulfon
 */
public class OldUtils {
	
    public static boolean isNumeric(String str) {  
      try {  
        double d = Double.parseDouble(str);  
      } catch(NumberFormatException nfe) {  
        return false;  
      }  
      return true;  
    }    

    public static void printIndent(PrintWriter out, int indent) {
        out.print(indent(indent));
    }

    public static String indent(int indent) {
        switch (indent) {
        case 8:
            return("        ");
        case 7:
            return("       ");
        case 6:
            return("      ");
        case 5:
            return("     ");
        case 4:
            return("    ");
        case 3:
            return("   ");
        case 2:
            return("  ");
        case 1:
            return(" ");
        default:
            StringBuffer buf = new StringBuffer();
            for (int i=0; i<indent; ++i) { buf.append(" "); }
            return buf.toString();
        }
    }

    public static String commaList(Object[] a) {
        return commaList(Arrays.asList(a).iterator());
    }

    public static String commaList(Collection c) {
        return commaList(c.iterator());
    }

    public static String commaList(Iterator i) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        printCommaList(pw, i);
        pw.close();
        return sw.toString();
    }


    /**
     * Given an iterator, prints it as a comma-delimited list
     * (actually a comma-and-space delimited list).  E.g. If the
     * iterator contains the strings { "my", "dog", "has fleas" } it
     * will print "my, dog, has fleas".
     *
     * @param out the stream to write to
     * @param i an iterator containing printable (toString) objects, e.g. strings
     **/
    public static void printCommaList(PrintWriter out, Iterator i) {
        boolean first = true;
        while (i.hasNext()) {
            if (first) first = false;
            else out.print(", ");
            out.print(i.next());
        }
    }

    /**
     * @returns true if all characters in the string are whitespace characters, or the string is empty
     **/
    public static boolean isWhitespace(String s) {
        for (int i=0; i<s.length(); ++i) {
            if (!Character.isWhitespace(s.charAt(i))) return false;
        }
        return true;
    }

    /**
     * Class encapsulating information from an exec call -- slightly
     * easier than the standard API
     **/
    public static class ExecInfo {
        public int exit;
        public String stdout;
        public String stderr;
    }

    /**
     * Wrapper for Runtime.exec. Takes input as a String. Times out
     * after sleep msec. Returns an object containing exit value,
     * standard output, and error output.
     * @param command the command-line to execute
     * @param input a string to pass to the process as standard input
     * @param sleep msec to wait before terminating process (if <= 0, waits forever)
     **/
/*    public static ExecInfo exec(String command, String input, long sleep)  throws IOException {
        Process process = null;
        ExecInfo info = new ExecInfo();
        try {
            Alarm a = null;
            if (sleep>0) {
                a = new Alarm(Thread.currentThread(), sleep);
                a.start();
            }

            process = Runtime.getRuntime().exec(command);

            if (input != null) {
                PrintWriter pw = new PrintWriter(process.getOutputStream());
                pw.print(input);
                pw.close();
            }

            info.stdout = IOUtils.readStream(process.getInputStream());

            info.stderr = IOUtils.readStream(process.getErrorStream());

            process.waitFor();
            if (a!=null)    a.stop = true;
        }
        catch (InterruptedIOException iioe) {
            throw new IOException("Process '" + command + "' took more than " + sleep/1000 + " sec");
        }
        catch (InterruptedException ie) {
            throw new IOException("Process '" + command + "' took more than " + sleep/1000 + " sec");
        }

        finally {
            if (process != null)
                process.destroy();
        }

        info.exit = process.exitValue();
        return info;
    }*/

    /**
     * Turn "Now is the time for all good men" into "Now is the time for..."
     * @param max maximum length of result string
     **/
    public static String abbreviate(String s, int max) {
        if (max < 4)
            throw new IllegalArgumentException("Minimum abbreviation is 3 chars");
        if (s.length() <= max) return s;
        // todo: break into words
        return s.substring(0, max-3) + "...";
    }

    /**
     * pad or truncate
     **/
    public static String pad(String s, int length) {
        if (s.length() < length) return s + indent(length - s.length());
        else return s.substring(0,length);
    }

    /**
     * returns the part of the second string from where it's different
     * from the first <p>
     * strdiff("i am a machine", "i am a robot") -> "robot"
     *
     **/
    public static String strdiff(String s1, String s2) {
        int at = strdiffat(s1, s2);
        if (at == -1)
            return "";
        return s2.substring(at);
    }

    /**
     * returns the index where the strings diverge<p>
     * strdiff("i am a machine", "i am a robot") -> 7<p>
     * @return -1 if they are the same
     *
     **/
    public static int strdiffat(String s1, String s2)
    {
        int i;
        for (i=0; i<s1.length() && i<s2.length(); ++i)
        {
            if (s1.charAt(i) != s2.charAt(i)) {
                break;
            }
        }
        if (i<s2.length() || i<s1.length())
            return i;
        return -1;
    }

    public static String strdiffVerbose(String expected, String actual)
    {
        int at = strdiffat(actual, expected);
        if (at == -1)
            return null;
        int length = 60;        // todo: parameterize
        int back = 20;          // todo: parameterize
        int start = at - 20;
        if (start < 3) start = 0;

        StringBuffer buf = new StringBuffer(length*2 + 100);
        buf.append("strings differ at character ").append(at);
        buf.append("\n");

        buf.append("Expected: ");
        appendWithEllipses(buf, expected, start, length);
        buf.append("\n");

        buf.append("  Actual: ");
        appendWithEllipses(buf, actual, start, length);
        buf.append("\n");

        return buf.toString();
    }

    private static void appendWithEllipses(StringBuffer buf, String s, int start, int length)
    {
        if (start > 0) buf.append("...");
        buf.append
            (javaEscape   // note that escapes may add \, making final string more than 60 chars
             (abbreviate  // abbreviate adds the final ... if necessary
              (s.substring(start), length)));
    }

    /**
     * count the number of occurences of ch inside s
     **/
    public static int count(String s, char ch) {
        int c=0;
        for (int i=0; i<s.length(); ++i) {
            if (s.charAt(i) == ch) c++;
        }
        return c;
    }

    public static String replace(String source, String target, String replacement) {
      return replace(source,target,replacement,false);
    }

    /**
     * Replace all occurences of target inside source with replacement.
     * E.g. replace("fee fie fo fum", "f", "gr") -> "gree grie gro grum"
     **/
    public static String replace(String source, String target, String replacement, boolean caseSensitive)
    {
        if (!caseSensitive) target=target.toLowerCase();
        // could use a regular expression, but this keeps it portable
        StringBuffer result = new StringBuffer(source.length());
        int i = 0, j = 0;
        int len = source.length();
        while (i < len) {
            if (!caseSensitive) j=source.toLowerCase().indexOf(target,i);
            else j = source.indexOf(target, i);
            if (j == -1) {
                result.append( source.substring(i,len) );
                break;
            }
            else {
                result.append( source.substring(i,j) );
                result.append( replacement );
                i = j + target.length();
            }
        }
        return result.toString();
    }

    /**
     * <p>
     *  Trim the right spacing off of a <code>String</code>.
     * </p>
     *
     * @param orig <code>String</code> to rtrim.
     * @return <code>String</code> - orig with no right spaces
     */
    public static String rtrim(String orig) {
        int len = orig.length();
        int st = 0;
        int off = 0;
        char[] val = orig.toCharArray();

        while ((st < len) && (val[off + len - 1] <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < orig.length())) ? orig.substring(st, len) : orig;
    }

    /**
     * <p>
     *  Trim the left spacing off of a <code>String</code>.
     * </p>
     *
     * @param orig <code>String</code> to rtrim.
     * @return <code>String</code> - orig with no left spaces
     */
    public static String ltrim(String orig) {
        int len = orig.length();
        int st = 0;
        int off = 0;
        char[] val = orig.toCharArray();

        while ((st < len) && (val[off + st] <= ' ')) {
            st++;
        }
        return ((st > 0) || (len < orig.length())) ? orig.substring(st, len) : orig;
    }

    /**
     * calculate the maximum length of all strings in i.  If i
     * contains other than strings, uses toString() value.
     **/
    public static int getMaxLength(Iterator i) {
        int max = 0;
        while (i.hasNext()) {
            String s = i.next().toString();
            int c = s.length();
            if (c>max) max=c;
        }
        return max;
    }

    /**
     * Prepares a string for output inside a JavaScript string,
     * e.g. for use inside a document.write("") command.
     *
     * Example:
     * <pre>
     * input string: He didn't say, "stop!"
     * output string: He didn\'t say, \"stop!\"
     * </pre>
     *
     * Deals with quotes and control-chars (tab, backslash, cr, ff, etc.)
     * Bug: does not yet properly escape Unicode / high-bit characters.
     *
     * @see #jsEscape(String, Writer)
     **/
    public static String jsEscape(String source) {
        if (source==null) return "";
        try {
            StringWriter sw = new StringWriter();
            jsEscape(source, sw);
            sw.flush();
            return sw.toString();
        }
        catch (IOException ioe) {
            // should never happen writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }

    /**
     * @see #javaEscape(String, Writer)
     **/
    public static String javaEscape(String source) {
        try {
            StringWriter sw = new StringWriter();
            javaEscape(source, sw);
            sw.flush();
            return sw.toString();
        }
        catch (IOException ioe) {
            // should never happen writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }


    /**
     * Prepares a string for output inside a JavaScript string,
     * e.g. for use inside a document.write("") command.
     *
     * Example:
     * <pre>
     * input string: He didn't say, "stop!"
     * output string: He didn\'t say, \"stop!\"
     * </pre>
     *
     * Deals with quotes and control-chars (tab, backslash, cr, ff, etc.)
     * Bug: does not yet properly escape Unicode / high-bit characters.
     *
     * @see #jsEscape(String)
     **/
    public static void jsEscape(String source, Writer out) throws IOException {
        stringEscape(source, out, true);
    }

    /**
     * Prepares a string for output inside a Java string,
     *
     * Example:
     * <pre>
     * input string: He didn't say, "stop!"
     * output string: He didn't say, \"stop!\"
     * </pre>
     *
     * Deals with quotes and control-chars (tab, backslash, cr, ff, etc.)
     * Bug: does not yet properly escape Unicode / high-bit characters.
     *
     * @see #jsEscape(String,Writer)
     **/
    public static void javaEscape(String source, Writer out) throws IOException {
        stringEscape(source, out, false);
    }

    private static void stringEscape(String source, Writer out, boolean escapeSingleQuote) throws IOException {
        char[] chars = source.toCharArray();
        for (int i=0; i<chars.length; ++i) {
            char ch = chars[i];
            switch (ch) {
            case '\b':  // backspace (ASCII 8)
                out.write("\\b");
                break;
            case '\t':  // horizontal tab (ASCII 9)
                out.write("\\t");
                break;
            case '\n':  // newline (ASCII 10)
                out.write("\\n");
                break;
            case 11:    // vertical tab (ASCII 11)
                out.write("\\v");
                break;
            case '\f':  // form feed (ASCII 12)
                out.write("\\f");
                break;
            case '\r':  // carriage return (ASCII 13)
                out.write("\\r");
                break;
            case '"':   // double-quote (ASCII 34)
                out.write("\\\"");
                break;
            case '\'':  // single-quote (ASCII 39)
                if (escapeSingleQuote) out.write("\\'");
                else out.write("'");
                break;
            case '\\':  // literal backslash (ASCII 92)
                out.write("\\\\");
                break;
            default:
                // todo: escape unicode / high-bit chars (JS works
                // with either \ u 000 or \ x 000 -- both take hex codes
                // AFAIK)
                out.write(ch);
                break;
            }
        }
    }



    /**
     *  Filter out Windows and Mac curly quotes, replacing them with
     *  the non-curly versions. Note that this doesn't actually do any
     *  checking to verify the input codepage. Instead it just
     *  converts the more common code points used on the two platforms
     *  to their equivalent ASCII values. As such, this method
     *  <B>should not be used</b> on ISO-8859-1 input that includes
     *  high-bit-set characters, and some text which uses other
     *  codepoints may be rendered incorrectly.
     *
     * @author Ian McFarland
     **/
    public static String uncurlQuotes(String input)
    {
        if (input==null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < input.length(); i++)
        {
            char ch = input.charAt(i);
            int code = (int) ch;
            if (code == 210 || code == 211 || code == 147 || code == 148)
            {
                ch = (char) 34; // double quote
            }
            else if (code == 212 || code == 213 || code == 145 || code == 146)
            {
                ch = (char) 39; // single quote
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * capitalize the first character of s
     **/
    public static String capitalize(String s) {
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    /**
     * lowercase the first character of s
     **/
    public static String lowerize(String s) {
        return s.substring(0,1).toLowerCase() + s.substring(1);
    }

    /**
     * turn String s into a plural noun (doing the right thing with
     * "story" -> "stories" and "mess" -> "messes")
     **/
    public static String pluralize(String s) {
        if (s.endsWith("y"))
            return s.substring(0, s.length()-1) + "ies";

        else if (s.endsWith("s"))
            return s + "es";

        else
            return s + "s";
    }

    public static boolean ok(String s) {
        return (!(s == null || s.equals("")));
    }

    public static String toUnderscore(String s) {
        StringBuffer buf = new StringBuffer();
        char[] ch = s.toCharArray();
        for (int i=0; i<ch.length; ++i) {
            if (Character.isUpperCase(ch[i])) {
                buf.append('_');
                buf.append(Character.toLowerCase(ch[i]));
            }
            else {
                buf.append(ch[i]);
            }
        }
        //System.err.println(s + " -> " + buf.toString());
        return buf.toString();
    }

    public static String stripWhitespace(String s) {
        StringBuffer buf = new StringBuffer();
        char[] ch = s.toCharArray();
        for (int i=0; i<ch.length; ++i) {
            if (Character.isWhitespace(ch[i])) {
                continue;
            }
            else {
                buf.append(ch[i]);
            }
        }
        return buf.toString();
    }

    public static String getStackTrace(Throwable t) {
        StringWriter s = new StringWriter();
        PrintWriter p = new PrintWriter(s);
        t.printStackTrace(p);
        p.close();
        return s.toString();
    }

    public static void sleep(long msec) {
        try {
            Thread.sleep(msec);
        }
        catch (InterruptedException ie) {}
    }

    static long millis=0;
    static long imillis=0;

    public static void initMillis() {
      millis=System.currentTimeMillis();
      imillis=millis;
    }

    public static void debugMillis(String s) {
      long newmillis=System.currentTimeMillis();
      System.out.println(s+" : "+(newmillis-millis)+"ms [Total: "+(newmillis-imillis)+"]");
      millis=newmillis;
    }


}
