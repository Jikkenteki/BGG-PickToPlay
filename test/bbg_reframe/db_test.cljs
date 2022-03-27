(ns bbg-reframe.db-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [bbg-reframe.model.db :refer [update-plays-in-games update-games-with-play]]))

(deftest update-games-with-play-test
  (testing "no plays"
    (let [games {"0" {:id "0"}
                 "1" {:id "1"}
                 "2" {:id "2"}
                 "3" {:id "3"}
                 "4" {:id "4"}}
          play {:id "21" :game-id "5"}]
      (is (= games (update-games-with-play games play)))))

  (testing "one play"
    (let [games {"0" {:id "0"}
                 "1" {:id "1"}
                 "2" {:id "2"}
                 "3" {:id "3"}
                 "4" {:id "4"}}
          play {:id "123"
                :date "2019-05-04",
                :game-id "3",
                :players '({:name "Dimitris Dranidis", :username "ddmits"}
                           {:name "Andreas", :username "adranidis"}
                           {:name "Kostas", :username "dimopoulosk"})}
          games' (update-games-with-play games play)]
      (is (= (get games "0") (get games' "0")))
      (is (= (get games "1") (get games' "1")))
      (is (= (get games "2") (get games' "2")))
      (is (= play (get-in games' ["3" :plays "123"])))
      (is (= (get games "4") (get games' "4")))))

  (testing "same play id does override"
    (let [games {"0" {:id "0"}
                 "1" {:id "1"}
                 "2" {:id "2"}
                 "3" {:id "3" :plays {"123" {:id "123"}}}
                 "4" {:id "4"}}
          play {:id "123"
                :date "2019-05-04",
                :game-id "3"}
          games' (update-games-with-play games play)]
      (is (= (get games "0") (get games' "0")))
      (is (= (get games "1") (get games' "1")))
      (is (= (get games "2") (get games' "2")))
      (is (= play (get-in games' ["3" :plays "123"])))
      (is (= (get games "4") (get games' "4"))))

    ;
    ))

(deftest update-plays-in-games-test
  (testing "no plays"
    (let [games {"0" {:id "0"}
                 "1" {:id "1"}
                 "2" {:id "2"}
                 "3" {:id "3"}
                 "4" {:id "4"}}
          plays '()]
      (is (= games (update-plays-in-games games plays)))))

  (testing "one play"
    (let [games {"0" {:id "0"}
                 "1" {:id "1"}
                 "2" {:id "2"}
                 "3" {:id "3"}
                 "4" {:id "4"}}
          plays [{:id "123"
                  :date "2019-05-04",
                  :game-id "3",
                  :players ({:name "Dimitris Dranidis", :username "ddmits"}
                            {:name "Andreas", :username "adranidis"}
                            {:name "Kostas", :username "dimopoulosk"})}]
          games' (update-plays-in-games games plays)]
      (is (= (get games "0") (get games' "0")))
      (is (= (get games "1") (get games' "1")))
      (is (= (get games "2") (get games' "2")))
      (is (= (first plays) (get-in games' ["3" :plays "123"])))
      (is (= (get games "4") (get games' "4")))))

  (testing "many play"
    (let [games {"0" {:id "0"}
                 "1" {:id "1"}
                 "2" {:id "2"}
                 "3" {:id "3"}
                 "4" {:id "4"}}
          plays [{:id "21"
                  :date "2019-05-04",
                  :game-id "2"}
                 {:id "22"
                  :date "2019-05-04",
                  :game-id "3"}
                 {:id "23"
                  :date "2019-05-04",
                  :game-id "3"}]
          games' (update-plays-in-games games plays)]
      (is (= (get games "0") (get games' "0")))
      (is (= (get games "1") (get games' "1")))
      (is (= (first plays) (get-in games' ["2" :plays "21"])))
      (is (= (second plays) (get-in games' ["3" :plays "22"])))
      (is (= (nth plays 2) (get-in games' ["3" :plays "23"])))
      (is (= (get games "4") (get games' "4")))))
    ;
  )

