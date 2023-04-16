(ns bbg-reframe.views.loginView.loginView
  (:require [re-frame.core :as re-frame]
            [clojure.string :refer [trim]]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]
            [bbg-reframe.forms.forms :refer [input-element]]
            [bbg-reframe.forms.utils :refer [if-nil?->value]]
            [bbg-reframe.forms.subs :as form-subs]
            [bbg-reframe.firebase.events :as fb-events]
            [bbg-reframe.login-view.events :as login-events]
            [bbg-reframe.login-view.subs :as login-subs]))

(defn save-games
  []
  (re-frame/dispatch [::fb-events/fb-save-games]))

(defn login-comp
  []
  (let [email @(re-frame/subscribe [::form-subs/get-value [:login-form :email]])
        password @(re-frame/subscribe [::form-subs/get-value [:login-form :password]])]
    [:div
     [:h1 "HMPWTP account"]
     [:h1 " Signed-in user email: " (if-nil?->value @(re-frame/subscribe [::login-subs/email]) "(not signed-in)")]
     [input-element {:class "input-box min-w-0 grow h-full"
                     :type :text
                     :placeholder "email"
                     :path [:login-form :email]
                     :post-fn trim}]
     [input-element {:class "input-box min-w-0 grow h-full"
                     :type :password
                     :placeholder "password"
                     :path [:login-form :password]}]

     [:button.button.min-w-fit.px-2.ml-1
      {:on-click #(re-frame/dispatch [::login-events/sign-in email password])} "Sign in"]
     [:button.button.min-w-fit.px-2.ml-1
      {:on-click #(re-frame/dispatch [::login-events/sign-up email password])} "Sign up"]
     [:button.button.min-w-fit.px-2.ml-1
      {:disabled (nil? @(re-frame/subscribe [::login-subs/email]))
       :on-click #(re-frame/dispatch
                   [::login-events/sign-out])} "Sign out"]
     [:button.button.min-w-fit.px-2.ml-1
      {:disabled (nil? @(re-frame/subscribe [::login-subs/email])) :on-click save-games} "Save games to account"]
     [:button.button.min-w-fit.px-2.ml-1
      {:disabled (nil? @(re-frame/subscribe [::login-subs/email])) :on-click #(re-frame/dispatch [::fb-events/fetch-games])} "Fetch games from account"]]))

(defn login-view-panel
  []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
   [naive-nav-bar]

   [:h1 "Login"]
   [login-comp]])