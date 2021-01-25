(ns core
  (:require  ["requestify" :as requestify]
             [clojure.string :as str]))

(def headers {:Access-Control-Allow-Origin "*"
              :Access-Control-Allow-Methods "*"
              :Access-Control-Allow-Headers "*"})

(def repo (.. js/process -env -GITHUB_REPO))
(def user (.. js/process -env -GITHUB_USERNAME))
(def agora-origin (.. js/process -env -AGORA_ORIGIN))
(def token (.. js/process -env -AGORA_TOKEN))
(def password (.. js/process -env -PASSWORD))

;; UTILS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn json->clj [json-string]
  (js->clj (.parse js/JSON json-string) :keywordize-keys true))

(defn clj->json [clj-map]
  (js/JSON.stringify (clj->js clj-map)))


(defn return-response [code body]
  (clj->js {:statusCode code
            :headers headers
            :body body}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def credentials
  (clj->json
   {:token token
    :user user
    :repo repo
    :origin agora-origin}))

(defn valid-event? [{:keys [method secret]}]
  (and (= method "POST") (= password secret) true))

(defn LOGIN [event context callback]
  (let [{:keys [httpMethod body]} (js->clj event :keywordize-keys true)
        event-params (assoc (json->clj body) :method httpMethod)]
    (if (valid-event? event-params)
      (callback nil (return-response 200 credentials))
      (callback nil (return-response 404 "Invalid request")))))
