(ns bbg-reframe.login-view.view
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.forms.forms :refer [input]]
            [bbg-reframe.forms.subs :as subs]
            [bbg-reframe.login-view.events :as events]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]
            [bbg-reframe.login-view.subs :as login-subs]
            [bbg-reframe.forms.utils :refer [if-nil?->value]]))

(defn login-comp
  []
  (let [form @(re-frame/subscribe [::subs/get-value [:login-form]])]
    [:div
     [:h1 "HMPWTP account"]
     [:h1 " User: " (if-nil?->value @(re-frame/subscribe [::login-subs/email]) "(none)")]
     [input "email" :text [:login-form :email]]
     [input "password" :password [:login-form :password]]
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-in (:email form) (:password form)])} "Sign in"]
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-up (:email form) (:password form)])} "Sign up"]
     [:button.button.min-w-fit.px-2.ml-1 {:on-click #(re-frame/dispatch [::events/sign-out])} "Sign out"]]))

(defn login-view-panel
  []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
   [naive-nav-bar]

   [:h1 "Login"]
   [login-comp]])