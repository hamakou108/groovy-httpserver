# httpserver-groovy

## Usage

- build package

```
$ gradle distZip
```

- start server

```
$ cd build/distributions && unzip httpserver-groovy.zip
$ cd httpserver-groovy && ./bin/httpserver-groovy
```

- connect server from client

```
$ curl http://localhost:12345
```
