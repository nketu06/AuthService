# AuthService

Use Spotless to automatically format the codebase according to the configured style rules.

```bash
./mvnw spotless:apply
```

This command:
- reformats Java files (and any other configured file types),
- updates files in place,
- helps keep formatting consistent before commit.

## MySQL container access

Use this command to open a shell inside the MySQL Docker container:

```bash
docker exec -it my-mysql bash
```
Once inside the container, you can access the MySQL command line interface with:
mysql -u root -p

Resources to learn more about spring boot:
https://www.baeldung.com
