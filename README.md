# jagrosk-persistence

This library tries to provide a Repository pattern generic implementation.

The base of its implementation are 3 interfaces:

- Repository: The data repository interface
- Entity: The data entity to be used within repository
- Attribute: An optional meta entity mapping

There are also a utility class, called `OneShotRepository`, to be used as a self transactioned repository to most simple operations.

Alongside those 3 base classes, a transaction API is provided, consisting in 2 base interfaces:

- Transaction: the transaction wrapper
- TransationBody: the transaction logic

With all those interfaces, some implmentations will be provided:

- DB: to be used with databases repositories
    - JPA: the first database implementation
    - Hibernate: will be the second one, if no other ORM engine is asked first
- File/Memory: simple to use file/memory backed repository, with the main purpose for unit testing or simple backed mockup, supporting the following formats:
    - Concurrent list in Memory
    - Serialization formats such as:
        + XML (JAX-B, other??)
        + JSON/HJSON
        + YAML
        + INI
