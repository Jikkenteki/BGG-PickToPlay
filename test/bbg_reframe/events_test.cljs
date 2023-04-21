(ns bbg-reframe.events-test
  (:require [bbg-reframe.panels.home.home-events :refer [make-available]]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest test-make-available
  (testing "make-all-available"
    (let [all-games
          {"1" {:id "1"}
           "2" {:id "2"}
           "3" {:id "3"}
           "4" {:id "4"}
           "5" {:id "5"}}
          available {:1 true, :2 true, :3 true, :4 true, :5 true}
          expected
          {"1" {:id "1" :available true}
           "2" {:id "2" :available true}
           "3" {:id "3" :available true}
           "4" {:id "4" :available true}
           "5" {:id "5" :available true}}]
      (is (= expected (make-available all-games available)))))

  (testing "make-some-available"
    (let [all-games
          {"1" {:id "1"}
           "2" {:id "2"}
           "3" {:id "3"}
           "4" {:id "4"}
           "5" {:id "5"}}
          available {:1 true, :3 true}
          expected
          {"1" {:id "1" :available true}
           "2" {:id "2" :available false}
           "3" {:id "3" :available true}
           "4" {:id "4" :available false}
           "5" {:id "5" :available false}}]
      (is (= expected (make-available all-games available)))))

  (testing "make-none-available"
    (let [all-games
          {"1" {:id "1"}
           "2" {:id "2"}
           "3" {:id "3"}
           "4" {:id "4"}
           "5" {:id "5"}}
          available {}
          expected
          {"1" {:id "1" :available false}
           "2" {:id "2" :available false}
           "3" {:id "3" :available false}
           "4" {:id "4" :available false}
           "5" {:id "5" :available false}}]
      (is (= expected (make-available all-games available))))))
