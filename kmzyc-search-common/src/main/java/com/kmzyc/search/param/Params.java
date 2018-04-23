package com.kmzyc.search.param;

/**
 * 请求参数
 * 
 * @author river
 * 
 */
public enum Params {

  qt {
    @Override
    public String val() {

      return this.name();
    }
  }, // the Request Handler (formerly known as the Query Type) - which Request
     // Handler should handle the request */

  q {
    @Override
    public String val() {

      return this.name();
    }
  }, // query string */

  sort {
    @Override
    public String val() {

      return this.name();
    }
  }, // sort order */

  fq {
    @Override
    public String val() {

      return this.name();
    }
  }, // Lucene query string(s) for filtering the results without affecting
     // scoring

  start {
    @Override
    public String val() {

      return this.name();
    }
  }, // zero based offset of matching documents to retrieve */

  rows {
    @Override
    public String val() {

      return this.name();
    }
  }, // number of documents to return starting at "start" */

  fl {
    @Override
    public String val() {

      return this.name();
    }
  }, // query and init param for field list */

  df {
    @Override
    public String val() {

      return this.name();
    }
  }, // default query field */

  wt {
    @Override
    public String val() {

      return this.name();
    }
  }, // the response writer type - the format of the response */

  qf {
    @Override
    public String val() {

      return this.name();
    }
  }, // query and init param for query fields */

  facet {
    @Override
    public String val() {

      return this.name();
    }
  }, // Should facet counts be calculated

  pf {
    @Override
    public String val() {

      return this.name();
    }
  }, // query and init param for phrase boost fields */

  mm {
    @Override
    public String val() {

      return this.name();
    }
  }, // query and init param for MinShouldMatch specification */

  ps {
    @Override
    public String val() {

      return this.name();
    }
  }, // query and init param for Phrase Slop value in phrase boost query (in
     // pf fields)

  ps2 {
    @Override
    public String val() {

      return this.name();
    }
  }, // default phrase slop for bigram phrases (pf2) */

  ps3 {
    @Override
    public String val() {

      return this.name();
    }
  }, // default phrase slop for bigram phrases (pf3) */

  qs {
    @Override
    public String val() {

      return this.name();
    }
  }, // query and init param for phrase Slop value in phrases explicitly
     // included in the user's query string ( in qf fields)

  bq {
    @Override
    public String val() {

      return this.name();
    }
  }, // query and init param for boosting query */

  bf {
    @Override
    public String val() {

      return this.name();
    }
  }, // query and init param for boosting functions

  gen {
    @Override
    public String val() {

      return this.name();
    }
  },

  defType {
    @Override
    public String val() {

      return this.name();
    }
  }; // query and init param for field list

  public abstract String val();
}
