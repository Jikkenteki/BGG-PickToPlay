(ns bbg-reframe.events-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            ;; [bbg-reframe.core :as core]
            [bbg-reframe.events
             :refer [fetch-next-from-queue-handler delay-between-fetches
                     fetched-games-ids]]))

;;
;; fetch-next-from-queue-handler
;;
(deftest test-fetch-next-from-queue-handler
  (testing "handler when empty queue and non-empty fetching")
  (let [cofx
        {:db {:queue #{}
              :fetching #{"1"}
              :game {}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= {} response))))

(deftest test-fetch-next-from-queue-handler-2
  (testing "handler when empty queue and empty fetching")
  (let [cofx
        {:db {:queue #{}
              :fetching #{}
              :games {"0" {:votes {}}
                      "1" {:votes nil}
                      "2" {:votes {}}}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= false (get-in response [:db :loading])))
    (is (= #{"0"} (get-in response [:db :fetching])))
    (is (= {:dispatch [:bbg-reframe.events/fetch-game "0"]
            :ms delay-between-fetches} (get response :dispatch-later)))))

(deftest test-fetch-next-from-queue-handler-3
  (testing "handler when non-empty queue")
  (let [cofx
        {:db {:queue #{"1"}
              :fetching #{"2"}
              :games {"0" {:votes {}}
                      "1" {:votes nil}
                      "2" {:votes nil}}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= true (get-in response [:db :loading])))
    (is (= #{"1" "2"} (get-in response [:db :fetching])))
    (is (= {:dispatch [:bbg-reframe.events/fetch-game "1"]
            :ms (* 2 delay-between-fetches)} (get response :dispatch-later)))))

(deftest test-fetch-next-from-queue-handler-4
  (testing "handler when non-empty queue with more than one id")
  (let [cofx
        {:db {:queue #{"1" "3" "4"} ;; first of set is "3"
              :fetching #{"2"}
              :games {"0" {:votes {}}
                      "1" {:votes nil}
                      "2" {:votes nil}
                      "3" {:votes nil}
                      "4" {:votes nil}}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= true (get-in response [:db :loading])))
    (is (= #{"1" "4"} (get-in response [:db :queue])))
    (is (= #{"3" "2"} (get-in response [:db :fetching])))
    (is (= {:dispatch [:bbg-reframe.events/fetch-game "3"]
            :ms (* 2 delay-between-fetches)} (get response :dispatch-later)))))

(deftest test-fetch-next-from-queue-handler-5
  (testing "handler when empty queue and fetching and all games fetched")
  (let [cofx
        {:db {:queue #{}
              :fetching #{}
              :games {"0" {:id "0" :votes {}}
                      "1" {:id "1" :votes {}}
                      "2" {:id "2" :votes {}}
                      "3" {:id "3" :votes {}}
                      "4" {:id "4" :votes {}}}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= false (get-in response [:db :loading])))
    (is (= #{} (get-in response [:db :queue])))
    (is (= #{} (get-in response [:db :fetching])))
    (is (= nil (get response :dispatch-later)))))



(deftest test-fetched-games-ids
  (testing "all fetched should be all games")
  (let [games {"0" {:id "0" :votes {}}
               "1" {:id "1" :votes {}}
               "2" {:id "2" :votes {}}
               "3" {:id "3" :votes {}}
               "4" {:id "4" :votes {}}}]
    (is (= #{"0" "1" "2" "3" "4"} (fetched-games-ids games)))))

(deftest test-fetched-games-ids-2
  (testing "some fetched")
  (let [games {"0" {:id "0" :votes nil}
               "1" {:id "1" :votes {}}
               "2" {:id "2" :votes nil}
               "3" {:id "3" :votes {}}
               "4" {:id "4" :votes {}}}]
    (is (= #{"1" "3" "4"} (fetched-games-ids games)))))

(deftest test-fetched-games-ids-3
  (testing "none fetched")
  (let [games {"0" {:id "0" :votes nil}
               "1" {:id "1" :votes nil}
               "2" {:id "2" :votes nil}
               "3" {:id "3" :votes nil}
               "4" {:id "4" :votes nil}}]
    (is (= #{} (fetched-games-ids games)))))

