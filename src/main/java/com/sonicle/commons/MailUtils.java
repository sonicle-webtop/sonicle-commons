package com.sonicle.commons;

/*
 * ====================================================================
 * Copyright (c) 1995-1999 Purple Technology, Inc. All rights
 * reserved.
 *
 * PLAIN LANGUAGE LICENSE: Do whatever you like with this code, free
 * of charge, just give credit where credit is due. If you improve it,
 * please send your improvements to alex@purpletech.com. Check
 * http://www.purpletech.com/code/ for the latest version and news.
 *
 * LEGAL LANGUAGE LICENSE: Redistribution and use in source and binary
 * forms, with or without modification, are permitted provided that
 * the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * 3. The names of the authors and the names "Purple Technology,"
 * "Purple Server" and "Purple Chat" must not be used to endorse or
 * promote products derived from this software without prior written
 * permission. For written permission, please contact
 * server@purpletech.com.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHORS AND PURPLE TECHNOLOGY ``AS
 * IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE
 * AUTHORS OR PURPLE TECHNOLOGY BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 *
 **/


import java.io.*;
import java.util.*;

public class MailUtils {
    

    /**
     * Turns funky characters into HTML entity equivalents<p>
     * e.g. <tt>"bread" & "butter"</tt> => <tt>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</tt>.
     * Update: supports nearly all HTML entities, including funky accents. See the source code for more detail.
     * @see #htmlunescape(String)
     **/
    public static String htmlescape(String s1) {
        StringBuffer buf = new StringBuffer();
        int i;
        for (i=0; i<s1.length(); ++i) {
            char ch = s1.charAt(i);
						int ich=(int)ch;
            String entity = getStringEntity(ich);
            if (entity == null) {
                if (ich > 128) {
                    buf.append("&#" + ich + ";");
                }
                else {
                    buf.append(ch);
                }
            }
            else {
                buf.append("&" + entity + ";");
            }
        }
        return buf.toString();
    }

    public static String htmlescape(char chars[], int start, int length) {
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<length; ++i) {
            char ch = chars[start+i];
						int ich=(int)ch;
            String entity = getStringEntity(ich);
            if (entity == null) {
                if (ich > 128) {
                    buf.append("&#" + ich + ";");
                }
                else {
                    buf.append(ch);
                }
            }
            else {
                buf.append("&" + entity + ";");
            }
        }
        return buf.toString();
    }
	
    /**
     * Given a string containing entity escapes, returns a string
     * containing the actual Unicode characters corresponding to the
     * escapes.
     *
     * Note: nasty bug fixed by Helge Tesgaard (and, in parallel, by
     * Alex, but Helge deserves major props for emailing me the fix).
     * 15-Feb-2002 Another bug fixed by Sean Brown <sean@boohai.com>
     *
     * @see #htmlescape(String)
     **/
	static int endOfEscape(String s, int start) {
		for(int i=0;i<8;++i) {
			int index=start+i;
			if (index>=s.length()) break;
			char c=s.charAt(index);
			if (c=='#' && i==0) continue;
			if (c==';' || (!Character.isLetter(c) && !Character.isDigit(c))) return index;
		}
		return -1;
	}

	static int endOfEscape(char chars[], int start) {
		for(int i=0;i<8;++i) {
			int index=start+i;
			if (index>=chars.length) break;
			if (chars[index]==';' || !Character.isLetter(chars[index])) return index;
		}
		return -1;
	}

    public static String htmlunescape(char chars[], int start, int length, boolean nbsp) {
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<length; ++i) {
						int index=start+i;
            char ch = chars[index];
            if (ch == '&') {
								int semi = endOfEscape(chars,index+1);
                if (semi == -1) {
                    buf.append(ch);
                    continue;
                }
								String entity = new String(chars,index+1,semi-1);
                int iso;
                if (entity.charAt(0) == '#') {
                    iso = Integer.parseInt(entity.substring(1));
                }
                else {
                    iso = getIntEntity(entity,nbsp);
                }
                if (iso == -1) {
										buf.append('&');
                    buf.append(entity);
										buf.append(';');
                }
                else {
                    buf.append((char)iso);
                }
                i = semi;
            }
            else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
	
    public static String htmlunescape(String s1, boolean nbsp) {
        StringBuffer buf = new StringBuffer();
        int i;
        for (i=0; i<s1.length(); ++i) {
            char ch = s1.charAt(i);
            if (ch == '&') {
                int semi = endOfEscape(s1,i+1);
                if (semi == -1) {
                    buf.append(ch);
                    continue;
                }
                String entity = s1.substring(i+1, semi);
                if (entity.length()>0) {
                  int iso;
                  if (entity.charAt(0) == '#') {
                      int radix=10;
                      String value=entity.substring(1);
                      char cc=entity.charAt(1);
                      if (cc=='x'||cc=='X') {
                          radix=16;
                          value=entity.substring(2);
                      }
                      iso = Integer.parseInt(value,radix);
                  }
                  else {
                    iso = getIntEntity(entity,nbsp);
                  }
                  if (iso == -1) {
                    buf.append('&');
                    buf.append(entity);
                    buf.append(';');
                  }
                  else {
                    buf.append((char)iso);
                  }
                } else buf.append('&');
                i = semi;
            }
            else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
	
	
    public static String htmlescapesource(String htmlsource) {

		// hold one token
		String token;
		// Do we have more lines of input to read?  Assumes true initially
		boolean more = true;
		// Are we inside a '<' '>' pair?  Assumes false initialy
		boolean insideTag = false;
		// An object to break up the input, splitting at '<' and '>'
		StringTokenizer st;

		StringBuffer sbuffer=new StringBuffer();

		// Otherwise construct a new input tokenizer.  This will split the line
		// at the characters '<' and '>'.  The string will consist of the line that
		// was read, and a system specific new line character, (This was removed by
		// the readLine call earlier.
		st = new StringTokenizer( htmlsource + System.getProperty("line.separator" ) , "<>", true);

		// While there are more characters '<' '>' get the text
		while( st.hasMoreTokens() ) {
			token = st.nextToken();

			// If the character was a '<' we are inside a tag.
			if ( token.equals( "<" ) ) {
				insideTag = true;
				sbuffer.append("<");
			// If the character was a '>' the tag is closed, mark us as being outside it
			} else if ( token.equals( ">" ) ) {
				insideTag = false;
				sbuffer.append(">");

				// Otherwise it is either plain text, or the text of the tag, i.e. H3
				// if we are currently inside a tag do nothing, otherwise we have the text
				// of the document, so send it to the output steam
			} else if ( insideTag) {
				sbuffer.append(token);
			} else {
				sbuffer.append(htmlescape(token) );
			}
		}

		return sbuffer.toString();
    }

    public static String htmlunescapesource(String htmlsource) {

		// hold one token
		String token;
		// Do we have more lines of input to read?  Assumes true initially
		boolean more = true;
		// Are we inside a '<' '>' pair?  Assumes false initialy
		boolean insideTag = false;
		// An object to break up the input, splitting at '<' and '>'
		StringTokenizer st;

		StringBuffer sbuffer=new StringBuffer();

		// Otherwise construct a new input tokenizer.  This will split the line
		// at the characters '<' and '>'.  The string will consist of the line that
		// was read, and a system specific new line character, (This was removed by
		// the readLine call earlier.
		st = new StringTokenizer( htmlsource + System.getProperty("line.separator" ) , "<>", true);

		// While there are more characters '<' '>' get the text
		while( st.hasMoreTokens() ) {
			token = st.nextToken();

			// If the character was a '<' we are inside a tag.
			if ( token.equals( "<" ) ) {
				insideTag = true;
				sbuffer.append("<");
			// If the character was a '>' the tag is closed, mark us as being outside it
			} else if ( token.equals( ">" ) ) {
				insideTag = false;
				sbuffer.append(">");

				// Otherwise it is either plain text, or the text of the tag, i.e. H3
				// if we are currently inside a tag do nothing, otherwise we have the text
				// of the document, so send it to the output steam
			} else if ( insideTag) {
				sbuffer.append(token);
			} else {
				sbuffer.append(htmlunescape(token,true) );
			}

		}

		return sbuffer.toString();
    }

    public static String htmlescapefixsource(String htmlsource) {

		// hold one token
		String token;
		// Do we have more lines of input to read?  Assumes true initially
		boolean more = true;
		// Are we inside a '<' '>' pair?  Assumes false initialy
		boolean insideTag = false;
		// An object to break up the input, splitting at '<' and '>'
		StringTokenizer st;

		StringBuffer sbuffer=new StringBuffer();

		// Otherwise construct a new input tokenizer.  This will split the line
		// at the characters '<' and '>'.  The string will consist of the line that
		// was read, and a system specific new line character, (This was removed by
		// the readLine call earlier.
		st = new StringTokenizer( htmlsource + System.getProperty("line.separator" ) , "<>", true);

		// While there are more characters '<' '>' get the text
		while( st.hasMoreTokens() ) {
			token = st.nextToken();

			// If the character was a '<' we are inside a tag.
			if ( token.equals( "<" ) ) {
				insideTag = true;
				sbuffer.append("<");
			// If the character was a '>' the tag is closed, mark us as being outside it
			} else if ( token.equals( ">" ) ) {
				insideTag = false;
				sbuffer.append(">");

			// Otherwise it is either plain text, or the text of the tag, i.e. H3
			// if we are currently inside a tag do nothing, otherwise we have the text
			// of the document, so send it to the output steam
			} else if ( insideTag) {
				sbuffer.append(token);
			} else {
				sbuffer.append(htmlescape(htmlunescape(token,true)) );
			}
		}

		return sbuffer.toString();
    }

	
	

    // see http://hotwired.lycos.com/webmonkey/reference/special_characters/
    static Object[][] entities = {
        {"#39", new int[] {39}},       // ' - apostrophe
        {"quot", new int[] {34}},      // " - double-quote
        {"amp", new int[] {38}},       // & - ampersand
        {"lt", new int[] {60}},        // < - less-than
        {"gt", new int[] {62}},        // > - greater-than
        {"nbsp", new int[] {160}},     // non-breaking space
        {"copy", new int[] {169}},     // � - copyright
        {"reg", new int[] {174}},      // � - registered trademark
        {"Agrave", new int[] {192}},   // � - uppercase A, grave accent
        {"Aacute", new int[] {193}},   // � - uppercase A, acute accent
        {"Acirc", new int[] {194}},    // � - uppercase A, circumflex accent
        {"Atilde", new int[] {195}},   // � - uppercase A, tilde
        {"Auml", new int[] {196}},     // � - uppercase A, umlaut
        {"Aring", new int[] {197}},    // � - uppercase A, ring
        {"AElig", new int[] {198}},    // � - uppercase AE
        {"Ccedil", new int[] {199}},   // � - uppercase C, cedilla
        {"Egrave", new int[] {200}},   // � - uppercase E, grave accent
        {"Eacute", new int[] {201}},   // � - uppercase E, acute accent
        {"Ecirc", new int[] {202}},    // � - uppercase E, circumflex accent
        {"Euml", new int[] {203}},     // � - uppercase E, umlaut
        {"Igrave", new int[] {204}},   // � - uppercase I, grave accent
        {"Iacute", new int[] {205}},   // � - uppercase I, acute accent
        {"Icirc", new int[] {206}},    // � - uppercase I, circumflex accent
        {"Iuml", new int[] {207}},     // � - uppercase I, umlaut
        {"ETH", new int[] {208}},      // � - uppercase Eth, Icelandic
        {"Ntilde", new int[] {209}},   // � - uppercase N, tilde
        {"Ograve", new int[] {210}},   // � - uppercase O, grave accent
        {"Oacute", new int[] {211}},   // � - uppercase O, acute accent
        {"Ocirc", new int[] {212}},    // � - uppercase O, circumflex accent
        {"Otilde", new int[] {213}},   // � - uppercase O, tilde
        {"Ouml", new int[] {214}},     // � - uppercase O, umlaut
        {"Oslash", new int[] {216}},   // � - uppercase O, slash
        {"Ugrave", new int[] {217}},   // � - uppercase U, grave accent
        {"Uacute", new int[] {218}},   // � - uppercase U, acute accent
        {"Ucirc", new int[] {219}},    // � - uppercase U, circumflex accent
        {"Uuml", new int[] {220}},     // � - uppercase U, umlaut
        {"Yacute", new int[] {221}},   // � - uppercase Y, acute accent
        {"THORN", new int[] {222}},    // � - uppercase THORN, Icelandic
        {"szlig", new int[] {223}},    // � - lowercase sharps, German
        {"agrave", new int[] {224}},   // � - lowercase a, grave accent
        {"aacute", new int[] {225}},   // � - lowercase a, acute accent
        {"acirc", new int[] {226}},    // � - lowercase a, circumflex accent
        {"atilde", new int[] {227}},   // � - lowercase a, tilde
        {"auml", new int[] {228}},     // � - lowercase a, umlaut
        {"aring", new int[] {229}},    // � - lowercase a, ring
        {"aelig", new int[] {230}},    // � - lowercase ae
        {"ccedil", new int[] {231}},   // � - lowercase c, cedilla
        {"egrave", new int[] {232}},   // � - lowercase e, grave accent
        {"eacute", new int[] {233}},   // � - lowercase e, acute accent
        {"ecirc", new int[] {234}},    // � - lowercase e, circumflex accent
        {"euml", new int[] {235}},     // � - lowercase e, umlaut
        {"igrave", new int[] {236}},   // � - lowercase i, grave accent
        {"iacute", new int[] {237}},   // � - lowercase i, acute accent
        {"icirc", new int[] {238}},    // � - lowercase i, circumflex accent
        {"iuml", new int[] {239}},     // � - lowercase i, umlaut
        {"eth", new int[] {240}},      // � - lowercase eth, Icelandic
        {"ntilde", new int[] {241}},   // � - lowercase n, tilde
        {"ograve", new int[] {242}},   // � - lowercase o, grave accent
        {"oacute", new int[] {243}},   // � - lowercase o, acute accent
        {"ocirc", new int[] {244}},    // � - lowercase o, circumflex accent
        {"otilde", new int[] {245}},   // � - lowercase o, tilde
        {"ouml", new int[] {246}},     // � - lowercase o, umlaut
        {"oslash", new int[] {248}},   // � - lowercase o, slash
        {"ugrave", new int[] {249}},   // � - lowercase u, grave accent
        {"uacute", new int[] {250}},   // � - lowercase u, acute accent
        {"ucirc", new int[] {251}},    // � - lowercase u, circumflex accent
        {"uuml", new int[] {252}},     // � - lowercase u, umlaut
        {"yacute", new int[] {253}},   // � - lowercase y, acute accent
        {"thorn", new int[] {254}},    // � - lowercase thorn, Icelandic
        {"yuml", new int[] {255}},     // � - lowercase y, umlaut
        {"euro", new int[] {8364}},    // Euro symbol
        {"nbsp", new int[] {9999}}    // Non breaking space
    };
    static Map e2i = new HashMap();
	static String i2e[] = new String[256];

    static {
		for(int i=0;i<i2e.length;++i) i2e[i]=null;

		for(int i=0; i<entities.length; ++i) {
			e2i.put(entities[i][0], entities[i][1]);
			int value=((int[])entities[i][1])[0];
			if (value<i2e.length) i2e[value]=(String)entities[i][0];
		}
    }
	
	static String getStringEntity(int i) {
		if (i<256) return i2e[i];
		if (i==8364) return "euro";
		if (i==9999) return "nbsp";
		return null;
	}
	
	static int getIntEntity(String s, boolean nbsp) {
		int iv[]=(int[])e2i.get(s);
		if (iv==null) return -1;
		int value=iv[0];
		if (!nbsp && value==9999) return -1;
		if (value==9999) value=' ';
		return value;
	}

	public static String getCharsetOrDefault(String contentType) {
		String charset=getCharsetOrNull(contentType);
		if (charset==null) charset=java.nio.charset.Charset.defaultCharset().name();
		return charset;
	}

	public static String getCharsetOrNull(String contentType) {
		String charset=null;
		int ix=contentType.indexOf(';');
		if(ix>0&&ix<(contentType.length()-1)) {
			String str=contentType.substring(ix+1).trim().toUpperCase();
			if(str.startsWith("CHARSET=")) {
				str=str.substring(8);
				ix=str.indexOf(';');
				if(ix>0) {
					str=str.substring(0, ix);
				}
				charset=str;
			}
		}
		return charset;
	}
	
	public static String decodeQString(String s, String encoding) throws Exception {
		String result=s;

		if (s.startsWith("=?") && s.endsWith("?=")) {

			String s2=s.substring(2,s.length()-2);
			int ix=s2.indexOf('?');
			if (ix>0) {
				String sencoding=s2.substring(0,ix);
				if (sencoding.equalsIgnoreCase(encoding)) {
					int iy=s2.indexOf('?',ix+1);
					if (iy>0) {
						String code=s2.substring(ix+1,iy);
						if (code.equalsIgnoreCase("Q")) {
							s=s2.substring(iy+1);
							StringBuffer sb=new StringBuffer();
							byte ba[]=new byte[2];
							for(int i=0;i<s.length();) {
								char c = s.charAt(i++);

								// Return '_' as ' '
								if (c == '_') sb.append(' ');
								else if (c == '=') {
									// QP Encoded atom. Get the next two bytes ..
									ba[0] = (byte)s.charAt(i++);
									ba[1] = (byte)s.charAt(i++);
									// .. and decode them
									try {
										sb.append(((char)parseInt(ba, 0, 2, 16)));
									} catch (NumberFormatException nex) {
										throw new Exception("Error in QP String " + nex.getMessage());
									}
								} else sb.append(c);
							}
							result=sb.toString();
						}
					}
				}
			}

		}

		return result;
	}

    /**
     * Convert the bytes within the specified range of the given byte
     * array into a String. The range extends from <code>start</code>
     * till, but not including <code>end</code>. <p>
     */
    public static String toString(byte[] b, int start, int end) {
			int size = end - start;
			char[] theChars = new char[size];

			for (int i = 0, j = start; i < size; )
					theChars[i++] = (char)b[j++];

			return new String(theChars);
    }
	
    /**
     * ===FROM JAVAMAIL com.sun.mail.util.ASCIIUtility
     * Convert the bytes within the specified range of the given byte 
     * array into a signed integer in the given radix . The range extends 
     * from <code>start</code> till, but not including <code>end</code>. <p>
     *
     * Based on java.lang.Integer.parseInt()
     */
    public static int parseInt(byte[] b, int start, int end, int radix)
		throws NumberFormatException {
        if (b == null)
            throw new NumberFormatException("null");

        int result = 0;
        boolean negative = false;
        int i = start;
        int limit;
        int multmin;
        int digit;

        if (end > start) {
            if (b[i] == '-') {
				negative = true;
				limit = Integer.MIN_VALUE;
				i++;
            } else {
            limit = -Integer.MAX_VALUE;
				}
            multmin = limit / radix;
            if (i < end) {
				digit = Character.digit((char)b[i++], radix);
				if (digit < 0) {
					throw new NumberFormatException(
						"illegal number: " + toString(b, start, end)
					);
				} else {
					result = -digit;
				}
            }
            while (i < end) {
				// Accumulating negatively avoids surprises near MAX_VALUE
				digit = Character.digit((char)b[i++], radix);
				if (digit < 0) {
					throw new NumberFormatException("illegal number");
				}
				if (result < multmin) {
					throw new NumberFormatException("illegal number");
				}
				result *= radix;
				if (result < limit + digit) {
					throw new NumberFormatException("illegal number");
				}
				result -= digit;
            }
        } else {
            throw new NumberFormatException("illegal number");
        }
        if (negative) {
            if (i > start + 1) {
				return result;
            } else {	/* Only got "-" */
				throw new NumberFormatException("illegal number");
            }
        } else {
            return -result;
        }
    }
	
	public static String decodeQString(String s) throws Exception {
		String result=s;

		if (s.startsWith("=?") && s.endsWith("?=")) {

			String s2=s.substring(2,s.length()-2);
			int ix=s2.indexOf('?');
			if (ix>0) {
				String sencoding=s2.substring(0,ix);
				int iy=s2.indexOf('?',ix+1);
				if (iy>0) {
					String code=s2.substring(ix+1,iy);
					if (code.equalsIgnoreCase("Q")) {
						s=s2.substring(iy+1);
						StringBuffer sb=new StringBuffer();
						byte ba[]=new byte[2];
						for(int i=0;i<s.length();) {
							char c = s.charAt(i++);

							// Return '_' as ' '
							if (c == '_') sb.append(' ');
							else if (c == '=') {
								// QP Encoded atom. Get the next two bytes ..
								ba[0] = (byte)s.charAt(i++);
								ba[1] = (byte)s.charAt(i++);
								// .. and decode them
								try {
									sb.append(((char)parseInt(ba, 0, 2, 16)));
								} catch (NumberFormatException nex) {
									throw new Exception("Error in QP String " + nex.getMessage());
								}
							} else sb.append(c);
						}
						result=sb.toString();
					}
				}
			}

		}

		return result;
	}

	
	//--------------------------------------------------------------------------
	// Do the work of reading from the input stream, in, and stripping the text
	// between the symbols '<', and '>'.  This result is then written to the
	// output stream out.
	//
	public static String HtmlToText_convert(String htmlstring) {

		// hold one token
		String token;
		// Do we have more lines of input to read?  Assumes true initially
		boolean more = true;
		// Are we inside a '<' '>' pair?  Assumes false initialy
		boolean insideTag = false;
		// An object to break up the input, splitting at '<' and '>'
		StringTokenizer st;

		StringBuffer sbuffer=new StringBuffer();

		boolean justAppended=false;

		// Otherwise construct a new input tokenizer.  This will split the line
		// at the characters '<' and '>'.  The string will consist of the line that
		// was read, and a system specific new line character, (This was removed by
		// the readLine call earlier.
		st = new StringTokenizer( htmlstring + System.getProperty("line.separator" ) , "<>", true);

		// While there are more characters '<' '>' get the text
		while( st.hasMoreTokens() ) {
			token = st.nextToken();
			// If the character was a '<' we are inside a tag.
			if ( token.equals( "<" ) ) {
				if (justAppended) sbuffer.append("\n");
				insideTag = true;
				justAppended=false;

			// If the character was a '>' the tag is closed, mark us as being outside it
			} else if ( token.equals( ">" ) ) {
				insideTag = false;

				// Otherwise it is either plain text, or the text of the tag, i.e. H3
				// if we are currently inside a tag do nothing, otherwise we have the text
				// of the document, so send it to the output steam
			} else if ( insideTag == false ) {
				token=token.trim();
				if (token.length()>0) {
					try {
						BufferedReader reader=new BufferedReader(new StringReader(token));
						String line=null;
						while((line=reader.readLine())!=null) {
							sbuffer.append(htmlunescape(line,true) );
						}
						reader.close();
						justAppended=true;
					} catch(IOException exc) {
						exc.printStackTrace();
					}
				}
			}

		}

		return sbuffer.toString();
  }
  
  public static String htmlToText(String htmlstring) {
    return (htmlstring==null?"":HtmlToText_convert(htmlstring));
  }


} 

