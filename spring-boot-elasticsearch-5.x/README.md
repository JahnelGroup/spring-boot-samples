# Spring Boot 2 + Elasticsearch 5.5.3

This sample demonstrates Spring Boot 2 connecting to an external dockerized Elasticsearch 5.5.3 instance.

## Configuration

* Spring-Boot is running at **localhost:8080**
* Elasticsearch is running at **localhost:9200**, but receives direct communication from Spring via 9300
* Kibana is running at **localhost:5601** (credentials are **elastic/changeme**)

## Use

Create an entity by POSTing data to the Spring-Boot service. It will be stored in Elasticsearch.

```bash
$ http POST :8080/persons firstName="Steven" lastName="Zgaljic"
HTTP/1.1 201 
Content-Type: application/json;charset=UTF-8
Date: Wed, 07 Feb 2018 21:46:08 GMT
Location: http://localhost:8080/persons/0
Transfer-Encoding: chunked

{
    "_links": {
        "person": {
            "href": "http://localhost:8080/persons/0"
        },
        "self": {
            "href": "http://localhost:8080/persons/0"
        }
    },
    "firstName": "Steven",
    "lastName": "Zgaljic"
}
```

You can retrieve it with a GET:

```bash
$ http :8080/persons
HTTP/1.1 200 
Content-Type: application/hal+json;charset=UTF-8
Date: Wed, 07 Feb 2018 21:46:12 GMT
Transfer-Encoding: chunked

{
    "_embedded": {
        "persons": [
            {
                "_links": {
                    "person": {
                        "href": "http://localhost:8080/persons/0"
                    },
                    "self": {
                        "href": "http://localhost:8080/persons/0"
                    }
                },
                "firstName": "Steven",
                "lastName": "Zgaljic"
            }
        ]
    },
    "_links": {
        "profile": {
            "href": "http://localhost:8080/profile/persons"
        },
        "self": {
            "href": "http://localhost:8080/persons{?page,size,sort}",
            "templated": true
        }
    },
    "page": {
        "number": 0,
        "size": 0,
        "totalElements": 1,
        "totalPages": 1
    }
}
```

The view from Kibana:

![screenshot from 2018-02-07 16-01-09](https://user-images.githubusercontent.com/26745523/35944104-f9cce15a-0c20-11e8-9d9c-86c5fd9d77bc.png)
