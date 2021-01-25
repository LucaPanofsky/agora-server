
# ISTRUZIONI

Http server e repl:
```bash
shadow-cljs watch app

shadow-cljs cljs-repl app
```

Dalla repl:
```clojure

(require '[app.core :as app])

```

## NETLIFY DEV

```bash
 netlify dev --functions=functions

 netlify functions:invoke FUNCTION --payload '{}'
```
