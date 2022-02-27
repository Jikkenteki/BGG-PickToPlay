(ns bbg-reframe.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            ;; [bbg-reframe.core :as core]
            [bbg-reframe.events :refer [fetch-next-from-queue-handler]]))


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
    (is (= [:bbg-reframe.events/fetch-game "0"] (get-in response [:dispatch-later :dispatch])))))

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
    (is (= [:bbg-reframe.events/fetch-game "1"] (get-in response [:dispatch-later :dispatch])))))