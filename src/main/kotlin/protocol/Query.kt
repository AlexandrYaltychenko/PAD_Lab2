package protocol

data class Query(val sort : Set<String> = mutableSetOf(),
                 val sortAsc : Boolean = true,
                 val filter : Set<String> = mutableSetOf(),
                 val group : Set<String> = mutableSetOf())