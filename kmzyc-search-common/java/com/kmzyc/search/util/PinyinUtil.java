package com.kmzyc.search.util;

import java.util.Set;

import com.google.common.collect.Sets;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转拼音
 * 
 * @author river
 */
public class PinyinUtil {

  private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

  /**
   * 私有构造器
   */
  private PinyinUtil() {

  }

  /**
   * 获取汉字拼音
   * 
   * @param source
   * @return
   * @throws BadHanyuPinyinOutputFormatCombination
   */
  public static String[] getNormalPy(String source) throws BadHanyuPinyinOutputFormatCombination {
    format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    format.setVCharType(HanyuPinyinVCharType.WITH_V);
    Set<String> result = Sets.newHashSet();
    result = getPy(source, 0, result);
    String[] py = new String[result.size()];
    int i = 0;
    for (String p : result) {
      py[i] = p;
      i++;
    }
    return py;
  }

  /**
   * 获取汉字简拼
   * 
   * @param source
   * @return
   * @throws BadHanyuPinyinOutputFormatCombination
   */
  public static String[] getJianPy(String source) throws BadHanyuPinyinOutputFormatCombination {
    format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    format.setVCharType(HanyuPinyinVCharType.WITH_V);
    Set<String> result = Sets.newHashSet();
    result = getJp(source, 0, result);
    String[] py = new String[result.size()];
    int i = 0;
    for (String p : result) {
      py[i] = p;
      i++;
    }
    return py;
  }

  public static Set<String> getPy(String source, int index, Set<String> result)
      throws BadHanyuPinyinOutputFormatCombination {
    if (index == source.length()) {
      return result;
    }
    char c = source.charAt(index);
    String[] pys = null;
    // 判断是否为汉字，如果是汉字转成拼音
    if (isChinese(c)) {
      pys = PinyinHelper.toHanyuPinyinStringArray(c, format);
    } else {
      // 非汉字保存原始值
      String t = source.substring(index, index + 1);
      pys = new String[1];
      pys[0] = t;
    }
    if (!result.isEmpty()) {
      Set<String> newResult = Sets.newHashSet();
      for (String prefix : result) {
        String temp = prefix;
        for (String suffix : pys) {
          temp += suffix;
          newResult.add(temp);
          temp = prefix;
        }
      }
      return getPy(source, index + 1, newResult);
    } else {
      for (String suffix : pys) {
        result.add(suffix);
      }
      return getPy(source, index + 1, result);
    }
  }

  public static Set<String> getJp(String source, int index, Set<String> result)
      throws BadHanyuPinyinOutputFormatCombination {
    if (index == source.length()) {
      return result;
    }
    char c = source.charAt(index);
    String[] pys;
    // 判断是否为汉字，如果是汉字转成拼音
    if (isChinese(c)) {
      pys = PinyinHelper.toHanyuPinyinStringArray(c, format);
      for (int i = 0; i < pys.length; i++) {
        String p = pys[i];
        pys[i] = p.substring(0, 1);
      }
    } else {
      // 非汉字保存原始值
      String t = source.substring(index, index + 1);
      pys = new String[1];
      pys[0] = t;
    }
    if (!result.isEmpty()) {
      Set<String> newResult = Sets.newHashSet();
      for (String prefix : result) {
        String temp = prefix;
        for (String suffix : pys) {
          temp += suffix;
          newResult.add(temp);
          temp = prefix;
        }
      }
      return getJp(source, index + 1, newResult);
    } else {
      for (String suffix : pys) {
        result.add(suffix);
      }
      return getJp(source, index + 1, result);
    }
  }

  // 中文字符在0x4E00和0x9FFF之间，中文标点在0xFF00之后
  // 0x4e00-0x9fbb
  /**
   * block == CJK_UNIFIED_IDEOGRAPHS // 中日韩统一表意文字 || block == CJK_COMPATIBILITY_IDEOGRAPHS //
   * 中日韩兼容字符 || block == CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A // 中日韩统一表意文字扩充A || block ==
   * GENERAL_PUNCTUATION // 一般标点符号, 判断中文的“号 || block == CJK_SYMBOLS_AND_PUNCTUATION // 符号和标点,
   * 判断中文的。号 || block == HALFWIDTH_AND_FULLWIDTH_FORMS // 半角及全角字符, 判断中文的，号
   * 
   * @param c
   * @return
   */
  public static boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
      return true;
    }
    return false;
  }

}
