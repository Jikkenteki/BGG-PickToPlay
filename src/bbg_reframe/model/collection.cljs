(ns bbg-reframe.model.collection)

(defn make-collection
  ([name]
   {:name name})
  ([id name]
   {:id id :name name})
  ([id name games]
   {:id id :name name :games games}))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn get-collections
  [collections]
  (reduce-kv (fn [m k v] (conj m (make-collection k (:name v) (:games v)))) [] collections))

(defn get-collection-names [collections]
  (map :name (get-collections collections)))

;; (defn get-collection-names [collections]
;;   (if (nil? collections)
;;     []
;;     (reduce-kv (fn [m _ v] (conj m (:name v))) [] collections)))

(defn collection-name-exists?
  [collections name]
  (in? (get-collection-names collections) name))


(defn collections-of-the-game
  [game-id collections]
  (filter not-empty (map (fn [collection] (if ((keyword game-id) (:games collection))
                                            collection
                                            nil)) collections)))

(comment

  (def collections {:-NTK-cZZcAJzjdrG6ifV {:name "ac"}
                    :af {:name "ab"}})

  (get-collections collections)
  (get-collection-names collections)

  ;
  )