# culture-map
[ ![Codeship Status for rafd/culture-map](https://app.codeship.com/projects/cff691d0-a498-0135-83a7-164f533c488b/status?branch=master)](https://app.codeship.com/projects/255061)

A collaborative map of varying cultural practices across the world

## To run

In a terminal
`rlwrap lein figwheel`

In a separate terminal
`lein repl`
`(start! 5929)` where 5929 is the port of your choice

Visit `http://localhost:5929/`

During development, make sure you have Dev Tools open, and 'Disable Cache' enabled (under the Network tab)

You can include a `config.edn` file in the root of your project directory (ex. human-db config).

### IntelliJ
Run REPL for `culture-map` and run `(start! 5929)`

In terminal, run `rlwrap lein figwheel`

## View it in action

CI build: http://culture-map.cfapps.io/
