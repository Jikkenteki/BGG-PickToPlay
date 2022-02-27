(ns bbg-reframe.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [bbg-reframe.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 3))))

(deftest fake-test-2
  (testing "fake description 2"
    (is (= 1 (+ 0 1)))))

