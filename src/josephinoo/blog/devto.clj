(ns josephinoo.blog.devto
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [malli.core :as m]))

(def DevTo
  [:map
   [:article [:map [:title string?]
              [:body_markdown string?]
              [:published boolean?]
              [:series string?]
              [:main_image string?]
              [:canonical_url string?]
              [:description string?]
              [:organization_id int?]
              [:tags [:vector string?]]]]])

(def ^:dynamic *base-url* "https://dev.to/api/articles")


(defn- devto->json [devto-article]
  (json/write-str devto-article :indent true))

(defn create-article [devto-article, api-key]
  (let [data  (-> {}
                  (assoc :article devto-article))
        headers {"Content-Type" "application/json"
                 "api-key" api-key}]
    (when (m/validate DevTo data)
      (client/post *base-url* {:body (.getBytes (devto->json data) "UTF-8")
                               :headers headers}))))


(comment
  (def devto-article {:title "Hello World"
                      :body_markdown "This is my first post on dev.to"
                      :published false
                      :series "Clojure"
                      :main_image "https://images.unsplash.com/photo-1612833603927-5e8a3e3b4b0f?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8Y2xvanVyZSUyMGJsb2d8ZW58MHx8MHx8&ixlib=rb-1.2.1&w=1000&q=80"
                      :canonical_url "https://dev.to/josephinoo/hello-world-1m0"
                      :description "This is my first post on dev.to"
                      :organization_id 123456
                      :tags ["clojure" "devto"]})

  (create-article devto-article ""))
