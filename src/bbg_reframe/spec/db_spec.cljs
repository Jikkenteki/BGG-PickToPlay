(ns bbg-reframe.spec.db-spec
  (:require [clojure.spec.alpha :as s]))

(def non-negative-number? (s/and number? #(>= % 0)))
(def string-or-nil? (s/or :string string? :nil nil?))
(def non-empty-string? (s/and string? #(seq %)))
(comment
  (false? (s/valid? non-empty-string? ""))
  (true? (s/valid? non-empty-string? "afdafa"))
;
  )

(s/def ::id non-empty-string?)

(s/def ::show-expansions? boolean?)
(s/def ::open-tab (s/or :tab #{:sliders-tab :user-name-tab :sort-tab} :nil nil?))
(s/def ::game-unchecked (s/keys))

(s/def ::user string-or-nil?)
(s/def ::cors-running boolean?)
(s/def ::fetches non-negative-number?)
(s/def ::form (s/keys :req-un
                      [::sort-id
                       ::take
                       ::players
                       ::threshold
                       ::time-available
                       ::show-expansions?]))
(s/def ::games (s/map-of ::id ::game-unchecked))
(s/def ::result (s/coll-of ::game-unchecked))
(s/def ::error string-or-nil?)
(s/def ::ui (s/keys :req-un [::open-tab]))
(s/def ::queue (s/coll-of ::id))
(s/def ::fetching (s/coll-of ::id))
(s/def ::network (s/keys :req-un [::cors-running]))

(comment
  #{}
  (defn in-set? [x] (#{1 2 5} x))
  (in-set? 5)
  (if (in-set? 3) "OK" "NO")
  ([1 2 3 4 5] 3)
  ;
  )

(s/def ::db
  (s/keys :req-un
          [::result
           ::form
           ::games
           ::queue
           ::fetching
           ::fetches
           ::bg-loading
           ::error
           ::network
           ::user
           ::ui]))
