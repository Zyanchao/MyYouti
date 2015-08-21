package com.youti.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 获取汉字的拼�?
 * @author poplar
 *
 */
public class PinyinUtils {
	
	public static String getPinyin(String str) {
		StringBuilder sb = new StringBuilder();

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);

		char[] charArray = str.toCharArray();
		// 遍历字符数组
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			// 如果是空格，则不转换
			if (Character.isWhitespace(c)) {
				continue;
			}
			if (c > 127) {
				// -128 -> 127
				// 肯定不是普�?字符^%$&*sadf
				try {
					// 转换每一个字符，�?>hei, �?-> dan , shan
					String s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
					sb.append(s);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
					sb.append(c);
				}
			} else {
				// 不是汉字，直接添�?
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
