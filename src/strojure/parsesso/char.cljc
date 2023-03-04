(ns strojure.parsesso.char
  "Basic function for parsing sequences of characters."
  (:refer-clojure :exclude [newline number?])
  (:require #?@(:bb [[clojure.string :as string]] :clj [] :default [[clojure.string :as string]])
            [strojure.parsesso.impl.char :as impl]
            [strojure.parsesso.parser :as p]))

#?(:clj  (set! *warn-on-reflection* true)
   :cljs (set! *warn-on-infer* true))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defn register-string-pred
  "Associates keyword `k` with predicate function of the [[is]] and
  [[is-not]] parsers."
  [k, f]
  (impl/register-string-pred-fn k f))

(register-string-pred :default impl/string-pred-default)
(register-string-pred :ic impl/string-pred-ignorecase)

(defn is
  "Returns parser and predicate for the character `c` which is in the supplied
  string of characters `s` (or a single character). The optional `pred-k`
  keyword refers to function `(fn [pred-k s] (fn [c] ...))` which returns custom
  predicate for chars against `s`. The new `pred-k` should be registered using
  [[register-string-pred]], predefined values are `:default` for default and
  `:ic` for case-insensitive matching.

      (def control-char (char/is \"EX\"))

      (def control-char-ignorecase (char/is \"ex\" :ic))
  "
  ([s]
   (p/token (impl/string-pred-default s)
            (delay (str (p/render s) " character"))))
  ([s, pred-k]
   (p/token (impl/string-pred-fn pred-k s)
            (delay (str (p/render s) " character")))))

(defn is-not
  "Returns parser and predicate for the character `c` which is _not_ in the
  supplied string of characters `s` (or a single character). See also [[is]]
  about optional `pred-k` argument."
  ([s]
   (p/token (complement (is s))
            (delay (str "not " (p/render s) " character"))))
  ([s, pred-k]
   (p/token (complement (is s pred-k))
            (delay (str "not " (p/render s) " character")))))

(defn regex
  "Returns parser and predicate for the character `c` matching regex pattern
  `re`."
  [re]
  (p/token (fn [c] (re-find re (str c)))
           (delay (str "character matching regex " (p/render re)))))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(def upper?
  "Parser and predicate for ASCII 7 bit upper-case letter character."
  (p/token (fn [c] #?(:bb
                      (re-find #"[A-Z]" (str c))
                      :clj
                      (let [c (unchecked-int (.charValue ^Character c))]
                        (and (<= 65 c) (<= c 90)))
                      :default
                      (re-find #"[A-Z]" (str c))))
           "upper-case ascii letter"))

(def lower?
  "Parser and predicate for ASCII 7 bit lower-case letter character."
  (p/token (fn [c] #?(:bb
                      (re-find #"[a-z]" (str c))
                      :clj
                      (let [c (unchecked-int (.charValue ^Character c))]
                        (and (<= 97 c) (<= c 122)))
                      :default
                      (re-find #"[a-z]" (str c))))
           "lower-case ascii letter"))

(def letter?
  "Parser and predicate for ASCII 7 bit letter character."
  (p/token (fn [c] #?(:bb
                      (re-find #"[a-zA-Z]" (str c))
                      :clj
                      (or (upper? c) (lower? c))
                      :default
                      (re-find #"[a-zA-Z]" (str c))))
           "ascii letter"))

(def number?
  "Parser and predicate for ASCII 7 bit number character."
  (p/token (fn [c] #?(:bb
                      (re-find #"[0-9]" (str c))
                      :clj
                      (let [c (unchecked-int (.charValue ^Character c))]
                        (and (<= 48 c) (<= c 57)))
                      :default
                      (re-find #"[0-9]" (str c))))
           "ascii number"))

(def letter-or-number?
  "Parser and predicate for ASCII 7 bit letter or number character."
  (p/token (fn [c] #?(:bb
                      (re-find #"[a-zA-Z0-9]" (str c))
                      :clj
                      (or (letter? c) (number? c))
                      :default
                      (re-find #"[a-zA-Z0-9]" (str c))))
           "ascii letter or number"))

(def white?
  "Parser and predicate for ASCII 7 bit whitespace character."
  (p/token (fn [c] #?(:bb
                      (string/index-of " \n\r\t\f" c)
                      :clj
                      (Character/isSpace c)
                      :default
                      (string/index-of " \n\r\t\f" c)))
           "whitespace character"))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(def newline
  "Parses a CRLF or LF end of line. Returns a `\\newline` character."
  (p/alt (is \newline)
         (p/after (is \return) (is \newline))))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defn str*
  "Builds string from (possibly nested) collections of parsed characters and
  strings. To be used with [[strojure.parsesso.parser/value]]."
  [x]
  (impl/str* x))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
