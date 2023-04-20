(ns bbg-reframe.collections-events-test
  (:require [bbg-reframe.firebase.firebase-events :as firebase-events]
            [bbg-reframe.model.collection :refer [in?]]
            [bbg-reframe.network-events :as network-events]
            [bbg-reframe.panels.collections.collections-events :refer [edit-collection-name-handle]]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest test-edit-collection-name-handle
  
  (testing "Edit success"
    (let [cofx {:db {:collections
                     {:-1 {:name "a"}
                      :-2 {:name "b"}}
                     :new-name "n"}}
          effects (edit-collection-name-handle cofx ["event id" [:-1 [:new-name]]])]
      (is (= "n" (get-in effects [:db :collections :-1 :name])))
      (is (= [::firebase-events/fb-set
              {:path ["collections" "-1" "name"] :data "n"}]
             (get-in effects [:dispatch])))))

  (testing "Edit failed - name already exists"
    (let [cofx {:db {:collections
                     {:-1 {:name "old-name"}
                      :-2 {:name "b"}}
                     :new-name "b"}}
          effects (edit-collection-name-handle cofx ["event id" [:-1 [:new-name]]])]
      (is (nil? (get-in effects [:db])))
      (is (in? (get-in effects [:dispatch]) ::network-events/set-error))
      (is (not (in? (get-in effects [:dispatch]) ::firebase-events/fb-set)))))

  (testing "No edit - same name"
    (let [cofx {:db {:collections
                     {:-1 {:name "old-name"}
                      :-2 {:name "b"}}
                     :new-name "old-name"}}
          effects (edit-collection-name-handle cofx ["event id" [:-1 [:new-name]]])]
      (is (empty? effects)))))

(comment
  (cljs.test/run-tests)
 ;
  )