(defproject game-of-life "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [cljfx "1.7.24"]
                 [criterium "0.4.6"]]
  :main ^:skip-aot gol.core
  :target-path "target/%s"
  :profiles {:uberjar { ; :aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

  ;; Plugin for GraalVM native image.
  ; :plugins [[io.taylorwood/lein-native-image "0.3.1"]]
  ; :native-image {:graal-bin "/home/vincebae/.sdkman/candidates/java/current/bin"
  ;                :name "gameOfLife"}
  ;; GraalVM profiles
  ; :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}
  ;            :test ;; e.g. lein with-profile +test native-image
  ;            {:native-image {:opts ["-H:+ReportUnsupportedElementsAtRuntime"
  ;                                   "--verbose"]}}
  ;            :uberjar {:aot :all
  ;                      :native-image {:opts ["-Dclojure.compiler.direct-linking=true"]}}})
  ;; default profiles
