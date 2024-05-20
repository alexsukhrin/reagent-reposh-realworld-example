(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require 
   [reagent.core :as r] 
   [ajax.core :refer [GET json-response-format]]))

(defonce artiales-state (atom nil))

(defn handler [response]
  (reset! artiales-state response)
  (js/console.log (str response)))

(defn error-handler [{:keys [status status-text]}]
  (js/console.log (str "something bad happened: " status " " status-text)))

(defn artiles-browse []
  (GET "https://conduit.productionready.io/api/articles?limit=20"
    :handler handler
    :response-format (json-response-format {:keywords? true})
    :error-handler error-handler))

(defn banner []
  [:div.banner>div.container 
   [:h1.logo-font "conduit"]
   [:p "A place to share your knowledge."]])

(defn article-preview [{:keys [tagList title description author favoritesCount createdAt]}]
  [:div.article-preview 
   [:div.article-meta
    [:a
     [:img {:src (:image author)}]]
    [:div.info
     [:a.author (:username author)]
     [:span.date (.toDateString (new js/Date createdAt))]]
    [:div.pull-xs-right
     [:button.btn.btn-sm.btn-outline-primary
      [:i.ion-heart favoritesCount]]]]
   [:a.preview-link
    [:h1 title]
    [:p description]
    [:span "readm more..."]
    [:ul.tag-list
     (for [tag tagList]
       ^{:key tag} [:li.tag-default.tag-pill.tag-outline tag])]]])

(defn articles [items]
  (if-not (seq items)
    [:div.article-preview "Loading..."]
    (if (= 0 (count items))
      [:div.article-preview "No articles are here... yet."]
      [:div
       (for [{:keys [slug] :as article} items]
         ^{:key slug} [article-preview article])])))

(defn main-view []
  [:div.col-md-9
   [:div.feed-toggle>ul.nav.nav-pills.outline-active>li.nav-item 
    [:a.nav-link.active {:href ""} "Global Feed"]]
   [articles (:articles @artiales-state)]])

(defn home []
  [:div.home-page 
   [banner]
   [:div.container.page>div.row
    [main-view]
    [:div.col-md-3>div.sidebar>p "Popular Tags"]]])

(defn header []
  [:nav.navbar.navbar-light>div.container
   [:a.navbar-brand (:appName "conduit")]])

(defn app []
  [:div
   [header]
   [home]])

(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (r/render [app] (.getElementById js/document "root")))

(defn ^:export main
  "Run application startup logic."
  []
  (artiles-browse)
  (render))
