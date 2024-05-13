(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]))

(def defaultState (atom {:appName "conduit"
                         :articles nil}))

(defonce articles-mock [{:title "Article Hello World!"}])

(defn banner []
  [:div.banner>div.container 
   [:h1.logo-font (:appName @defaultState)]
   [:p "A place to share your knowledge."]])

(defn articles []
  [:div.article-preview "Loading..."])

(mapv (fn [x] [:h2 (get x :title)]) articles-mock)

(defn main-view []
  [:div.col-md-9
   [:div.feed-toggle>ul.nav.nav-pills.outline-active>li.nav-item 
    [:a.nav-link.active {:href ""} "Global Feed"]]
   [articles]])

(defn home []
  [:div.home-page 
   [banner]
   [:div.container.page>div.row
    [main-view]
    [:div.col-md-3>div.sidebar>p "Popular Tags"]]])

(defn header []
  [:nav.navbar.navbar-light>div.container
   [:a.navbar-brand (:appName @defaultState)]])

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
  (render))
