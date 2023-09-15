(ns bbg-reframe.router
  (:require [bbg-reframe.events :as events]
            [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [re-frame.core :as re-frame]))

(def routes
  ["/" {""   :home
        "fb" :fb
        "login" :login-view
        "collections" :collections-view
        "game/" {"" :home
                 [:id ""] :game-view}
        "collection/" {"" :home
                       [:id ""] :collection-view}}])

(defn requires-auth?
  [route]
  (contains? #{:collections-view
               :collection-view}
             (:handler route)))

(defn parse
  "Parse a url string and return a map with the :handler keyword according to
   the routes table."
  [url]
  (bidi/match-route routes url))

(defn url-for
  [& args]
  (apply bidi/path-for (into [routes] args)))

(defn dispatch
  [route]
  (let [panel (keyword (str (name (:handler route)) "-panel"))
        path (:handler route)
        params (:route-params route)
        authenticated? (not (nil? (fb-reframe/get-current-user-uid)))
        ;; (seq @(re-frame/subscribe [::login-subs/email]))
        ]
    ;; (re-frame/dispatch [::events/set-active-panel panel])
    (println "ROUTING: authenticated?" authenticated?)

    (if (and (requires-auth? route) (not authenticated?))
      (do (re-frame/dispatch [::events/navigate [:home]])
          (re-frame/dispatch [::events/set-route {:params {} :panel :home-panel :path :home}]))
      (re-frame/dispatch [::events/set-route {:params params :panel panel :path path}]))))

(defonce history
  (pushy/pushy dispatch parse))

(defn navigate!
  [handler]
  ;; (pushy/set-token! history (url-for handler))
  (pushy/set-token! history (apply url-for handler)))

(defn is-route-current?
  [route]
  (= route
     (:handler (bidi/match-route routes (.. js/window -location -pathname)))))


(defn start!
  []
  (pushy/start! history))

(re-frame/reg-fx
 :navigate
 (fn [handler]
   (navigate! handler)))

(comment
  (parse "/")
  (dispatch (parse "/"))
  ;
  (parse "/game/28720")
  (url-for :collections-view)
  (url-for :collection-view :id 3)
  (apply url-for [:game-view :id 10])

  (parse "/")
  (url-for :home)
  (apply url-for [:home])
 ;

  history
  (def handler [:home])
  (apply url-for handler)

  (bidi/match-route routes "/")
  (bidi/match-route routes "/game")
  (bidi/match-route routes "/game/123")



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

  (:handler (bidi/match-route routes (.. js/window -location -pathname)))

  (navigate! [:home])

  (navigate! [:fb])
  (navigate! [:game-view :id "28720"])
  (navigate! [:collection-view :id "1"])
  (navigate! [:collections-view])

  (def route (parse "/game/102680"))
  route

  (parse "/")
  (parse "/fb")
  (parse "/game/102680")
  (def route (parse "/fb"))
  route

  (keyword (str (name (:handler route)) "-panel"))

  (is-route-current? :login-view)

  (dispatch route)
  ;
  )



