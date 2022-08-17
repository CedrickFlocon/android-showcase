# android-showcase
Android Showcase application

This project aims to show a modern architecture of an android project. To emphasise the structure only few library as been used.

## Architecture

### Modular
This project use a 3 layers architecture (presentation, domain, data) by feature (search, user ...).

<img src="docs/architecture.png" />


### Global
Because of `exception` mechanism (allow pass through layers), we will avoid using them.
`Either` is used between `data` & `domain` and then we will use `State`.

### Domain layers
Domain have all entities & use case that define a feature.

### Data layers
Expose data to the `domain` layer through a dependency inversion. Data can come from http, database but also GPS or web socket like push...

### Presentation layers
Define screen rendering with an MVVM pattern.

### Core
All transverse component is store in sub module of `core`.
e.g :   
`:core:presentation:design` => design system of the application share to all `:feature:presentation`
`:core:data:graphql` => graphql configuration share to all `:feature:data`


## Setup

To avoid OAuth setup, GQL API implementation use a personal access token.  
Create a Github access token and add it in `local.properties` under `github.bearer` key.
