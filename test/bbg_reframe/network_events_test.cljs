(ns bbg-reframe.network-events-test
  (:require [bbg-reframe.config :refer [delay-between-fetches]]
            [bbg-reframe.network-events
             :refer [fetch-next-from-queue-handler fetched-game-handler
                     fetched-games-ids]]
            [cljs.test :refer-macros [deftest testing is]]))

;;
;; fetch-next-from-queue-handler
;;
(deftest test-fetch-next-from-queue-handler
  (testing "handler when empty queue and non-empty fetching")
  (let [cofx
        {:db {:network {:queue #{}
                        :fetching #{"1"}}
              :game {}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= {} response))))

(deftest test-fetch-next-from-queue-handler-1
  (testing "handler when non empty queue and empty fetching")
  (let [cofx
        {:db {:network {:queue #{"1"}
                        :fetching #{}}
              :game {}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= true (get-in response [:db :network :loading])))
    (is (= #{} (get-in response [:db :network :queue])))
    (is (= #{"1"} (get-in response [:db :network :fetching])))
    (is (= {:dispatch [:bbg-reframe.network-events/fetch-game "1"]
            :ms delay-between-fetches} (get response :dispatch-later)))))

(deftest test-fetch-next-from-queue-handler-2
  (testing "handler when empty queue and empty fetching")
  (let [cofx
        {:db {:network {:queue #{}
                        :fetching #{}}
              :games {"0" {:id "0" :votes {}}
                      "1" {:id "1" :votes nil} ;; non fetched will be fetched
                      "2" {:id "2" :votes {}}}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= false (get-in response [:db :network :loading])))
    (is (= #{"1"} (get-in response [:db :network :fetching])))
    (is (= {:dispatch [:bbg-reframe.network-events/fetch-game "1"]
            :ms delay-between-fetches} (get response :dispatch-later)))))

(deftest test-fetch-next-from-queue-handler-3
  (testing "handler when non-empty queue")
  (let [cofx
        {:db {:network {:queue #{"1"}
                        :fetching #{"2"}}
              :games {"0" {:id "0" :votes {}}
                      "1" {:id "1" :votes nil}
                      "2" {:id "2" :votes nil}}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= true (get-in response [:db :network :loading])))
    (is (= #{"1" "2"} (get-in response [:db :network :fetching])))
    (is (= {:dispatch [:bbg-reframe.network-events/fetch-game "1"]
            :ms (* 2 delay-between-fetches)} (get response :dispatch-later)))))

(deftest test-fetch-next-from-queue-handler-4
  (testing "handler when non-empty queue with more than one id")
  (let [cofx
        {:db {:network {:queue #{"1" "3" "4"} ;; first of set is "3"
                        :fetching #{"2"}}
              :games {"0" {:votes {}}
                      "1" {:votes nil}
                      "2" {:votes nil}
                      "3" {:votes nil}
                      "4" {:votes nil}}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= true (get-in response [:db :network :loading])))
    (is (= #{"1" "4"} (get-in response [:db :network :queue])))
    (is (= #{"3" "2"} (get-in response [:db :network :fetching])))
    (is (= {:dispatch [:bbg-reframe.network-events/fetch-game "3"]
            :ms (* 2 delay-between-fetches)} (get response :dispatch-later)))))

(deftest test-fetch-next-from-queue-handler-5
  (testing "handler when empty queue and fetching and all games fetched")
  (let [cofx
        {:db {:network {:queue #{}
                        :fetching #{}}
              :games {"0" {:id "0" :votes {}}
                      "1" {:id "1" :votes {}}
                      "2" {:id "2" :votes {}}
                      "3" {:id "3" :votes {}}
                      "4" {:id "4" :votes {}}}}}
        response (fetch-next-from-queue-handler cofx nil)]
    (is (= false (get-in response [:db :network :loading])))
    (is (= #{} (get-in response [:db :network :queue])))
    (is (= #{} (get-in response [:db :network :fetching])))
    (is (= nil (get response :dispatch-later)))))

;;
;; fetched-games-ids
;; 
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



(deftest test-fetched-game-handler-0
  (testing "with empty queue")
  (let [cofx
        {:db {:games {"0" {:id "0" :votes {}}
                      "1" {:id "1" :votes {}}
                      "2" {:id "2" :votes {}}
                      "3" {:id "3" :votes {}}
                      "4" {:id "4" :votes {}}}
              :network {:fetches 5
                        :queue #{}
                        :fetching #{"1" "2" "3"}}}}
        game-id "3"
        game-votes {"5" "votes"}
        actual (fetched-game-handler cofx game-id game-votes "boardgame" 2.5)]
    (is (= nil (get-in actual [:db :error])))
    (is (= #{"1" "2"} (get-in actual [:db :network :fetching])))
    (is (= 6 (get-in actual [:db :network :fetches])))
    (is (= game-votes (get-in actual [:db :games "3" :votes])))
    (is (= 2.5 (get-in actual [:db :games "3" :weight])))
    (is (= [[:dispatch [:bbg-reframe.network-events/update-result]]] (get-in actual [:fx])))))

(deftest test-fetched-game-handler-1
  (testing "with non-empty queue")
  (let [cofx
        {:db {:games {"0" {:id "0" :votes {}}
                      "1" {:id "1" :votes {}}
                      "2" {:id "2" :votes {}}
                      "3" {:id "3" :votes {}}
                      "4" {:id "4" :votes {}}}
              :network {:fetches 5
                        :queue #{"9"}
                        :fetching #{"1" "2" "3"}}}}
        game-id "3"
        game-votes {"5" "votes"}
        actual (fetched-game-handler cofx game-id game-votes "boardgame" 3.0)]
    (is (= nil (get-in actual [:db :error])))
    (is (= #{"1" "2"} (get-in actual [:db :network :fetching])))
    (is (= 6 (get-in actual [:db :network :fetches])))
    (is (= 3.0 (get-in actual [:db :games "3" :weight])))
    (is (= game-votes (get-in actual [:db :games "3" :votes])))
    (is (= [[:dispatch [:bbg-reframe.network-events/fetch-next-from-queue]]] (get-in actual [:fx])))))


