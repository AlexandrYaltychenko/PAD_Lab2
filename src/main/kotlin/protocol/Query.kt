package protocol

data class Query(val sort : Set<String> = mutableSetOf(),
                 val filter : Set<String> = mutableSetOf(),
                 val group : Set<String> = mutableSetOf(),
                 val select : Set<String> = mutableSetOf())