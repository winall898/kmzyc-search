package com.kmzyc.search.app.index;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.lang.StringUtils;

/**
 * 对字符进行分词，对分词后的结果进行组合排列
 * 
 * @author zhoulinhong
 * @since 20160523
 */

public enum TermCreater {

  instance {

    private final Pattern pattern = Pattern.compile("[^a-zA-Z\u4e00-\u9fa5]+");

    @Override
    public Set<String> createTerm(String productName) {
      Set<String> result = new HashSet<String>();
      if (StringUtils.isBlank(productName)) {

        return result;
      }

      // 存储原始名称
      result.add(productName);

      // 切词
      List<Term> terms = ToAnalysis.parse(productName);
      for (Term term : terms) {
        String name = term.getName();
        // 跳过空字符串
        if (StringUtils.isBlank(name)) {

          continue;
        }

        // 跳过单字符串
        if (name.length() < 2) {

          continue;
        }

        // 跳过不包含任何中文与英文字符串
        Matcher m = pattern.matcher(name);
        if (m.matches()) {

          continue;
        }

        String nature = term.getNatrue().natureStr;
        if (null != nature) {
          if (nature.startsWith("n")) { // 名词
            result.add(name);
          }
        }
      }

      return result;
    }
  };

  public abstract Set<String> createTerm(String productName);
}
