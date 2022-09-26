(ns strojure.parsesso.text-test
  (:require [clojure.string :as string]
            [clojure.test :as test :refer [deftest testing]]
            [strojure.parsesso.core :as p]
            [strojure.parsesso.text :as t]))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(comment
  (test/run-tests))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

;; TODO: Look at pos and remaining input.
(defn- p
  "Parses test input using given parser. Returns custom map with test result."
  [parser input]
  (let [result (t/parse parser input)]
    (if (p/error? result)
      (-> (select-keys result [:consumed])
          (assoc :error (-> (:error result) (str) (string/split-lines))))
      (select-keys result [:consumed :value]))))

(defn- c
  "Cross-platform char."
  [s]
  #?(:cljs s, :clj (first s)))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(deftest any-char-t
  (test/are [expr result] (= result expr)

    (p t/any-char
       "a")
    {:consumed true, :value (c "a")}

    (p t/any-char
       "")
    {:consumed false, :error ["at line 1, column 1:"
                              "unexpected end of input"]}

    ))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(deftest char-of-t
  (test/are [expr result] (= result expr)

    (p (t/char-of "abc")
       "a")
    {:consumed true, :value (c "a")}

    (p (t/char-of "abc")
       "b")
    {:consumed true, :value (c "b")}

    (p (t/char-of "abc")
       "c")
    {:consumed true, :value (c "c")}

    (p (t/char-of "abc")
       "d")
    {:consumed false, :error ["at line 1, column 1:"
                              "unexpected \"d\""
                              "expecting (char-of \"abc\")"]}

    (p (t/char-of "abc")
       "")
    {:consumed false, :error ["at line 1, column 1:"
                              "unexpected end of input"
                              "expecting (char-of \"abc\")"]}

    ))

(deftest char-of-not-t
  (test/are [expr result] (= result expr)

    (p (t/char-of-not "abc")
       "x")
    {:consumed true, :value (c "x")}

    (p (t/char-of-not "abc")
       "a")
    {:consumed false, :error ["at line 1, column 1:"
                              "unexpected \"a\""
                              "expecting (char-of-not \"abc\")"]}

    (p (t/char-of-not "abc")
       "")
    {:consumed false, :error ["at line 1, column 1:"
                              "unexpected end of input"
                              "expecting (char-of-not \"abc\")"]}

    ))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(deftest letter-t
  (test/are [expr result] (= result expr)

    (p t/letter
       "a")
    {:consumed true, :value (c "a")}

    (p t/letter
       "1")
    {:consumed false, :error ["at line 1, column 1:"
                              "unexpected \"1\""
                              "expecting letter"]}

    (p t/letter
       "")
    {:consumed false, :error ["at line 1, column 1:"
                              "unexpected end of input"
                              "expecting letter"]}

    ))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(deftest string-t
  (test/are [expr result] (= result expr)

    (p (t/string "abc")
       "abc")
    {:consumed true, :value "abc"}

    (p (t/string "abc")
       "ab")
    {:consumed true, :error ["at line 1, column 3:"
                             "unexpected end of input"
                             "expecting \"c\" in (string \"abc\")"]}

    (p (t/string "abc")
       "")
    {:consumed false, :error ["at line 1, column 1:"
                              "unexpected end of input"
                              "expecting \"a\" in (string \"abc\")"]}

    (p (t/string "abc")
       "abx")
    {:consumed true, :error ["at line 1, column 3:"
                             "unexpected \"x\""
                             "expecting \"c\" in (string \"abc\")"]}

    (p (t/string "abc")
       "xyz")
    {:consumed false, :error ["at line 1, column 1:"
                              "unexpected \"x\""
                              "expecting \"a\" in (string \"abc\")"]}

    ))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
