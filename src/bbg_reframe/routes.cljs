(ns bbg-reframe.routes
  (:require
   [bidi.bidi :as bidi]
   [pushy.core :as pushy]
   [re-frame.core :as re-frame]
   [bbg-reframe.events :as events]))

(defmulti panels identity)
(defmethod panels :default []
  [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200 "No panel found for this route."])

(def routes
  (atom
   ["/" {""   :home
         "fb" :fb
         "login" :login-view
         "game" {"" :home
                 ["/" :id] :game-view}}]))

(defn parse
  "Parse a url string and return a map with the :handler keyword according to
   the @routes table."
  [url]
  (bidi/match-route @routes url))

(defn url-for
  [& args]
  (apply bidi/path-for (into [@routes] args)))

(comment
  (parse "/game/10")
  (apply url-for [:games :id 10])

  (parse "/")
  (url-for :home)
  (apply url-for [:home])
 ;
  )

(defn dispatch
  [route]
  (let [panel (keyword (str (name (:handler route)) "-panel"))]
    ;; (re-frame/dispatch [::events/set-active-panel panel])
    (re-frame/dispatch [::events/set-route {:route-params (:route-params route) :panel panel}])))

(comment
  (parse "/")
  (dispatch (parse "/"))
  ;
  )

(defonce history
  (pushy/pushy dispatch parse))

(defn navigate!
  [handler]
  ;; (pushy/set-token! history (url-for handler))
  (pushy/set-token! history (apply url-for handler)))

(defn start!
  []
  (pushy/start! history))

(re-frame/reg-fx
 :navigate
 (fn [handler]
   (navigate! handler)))



(comment
  history
  (def handler [:home])
  (apply url-for handler)

  (def handler [:game-view :id 10])
  handler

  (apply url-for handler) ;; "/"

  (apply url-for handler)

  (parse "/") ;; {:handler :home}
  (def route (parse "/"))
  (:handler route)  ;; :home
  (keyword (str (name (:handler route)) "-panel")) ;; :home-panel


  (def route (parse "/game/1"))
  (keyword (str (name (:handler route)) "-panel")) ;; :home-panel


  (navigate! [:home])

  (navigate! [:fb])
  (navigate! [:game-view :id 102680])

  (def route (parse "/game/102680"))
  route

  (parse "/")
  (parse "/fb")
  (parse "/game/102680")
  (def route (parse "/fb"))
  route

  (keyword (str (name (:handler route)) "-panel"))


  (dispatch route)
  ;
  )