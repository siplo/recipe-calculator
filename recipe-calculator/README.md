# Recipe Calculator

Responsive Quarkus 3.27 / Java 21 web app that scales recipe ingredients based on the user’s meat weight input. The UI enforces name entry, asks whether the user is sober, and — if the user is not sober — confirms every action before calculating quantities. Fonts and controls are intentionally large for readability on any device.

## Quickstart

```bash
./mvnw quarkus:dev
```

- Dev UI: <http://localhost:8080/q/dev/>
- Web UI: <http://localhost:8080/>

## Using the web UI

1) Enter your name to proceed.
2) Answer the sobriety question.
   - If you answer **No**, each subsequent action requires confirmation ("you are drunk…" prompt).
3) Choose a recipe from the list.
4) Enter the actual kilograms of meat (recipes are authored for 10 kg by default).
5) Review the scaled ingredient list generated for your weight.

## REST API

- **List recipes:** `GET /api/recipes`
- **Scale a recipe:** `POST /api/recipes/{id}/scale` with body `{ "weightKg": <number> }`

## Recipe configuration

Recipes are JSON files under `src/main/resources/recipes/` and referenced by `index.json`. Each recipe defines a base batch size (default 10 kg) and ingredient quantities:

```json
{
  "id": "beef-goulash",
  "name": "Beef Goulash",
  "baseWeightKg": 10,
  "ingredients": [
    { "name": "Beef", "unit": "kg", "amount": 10 },
    { "name": "Paprika", "unit": "g", "amount": 120 }
  ]
}
```

To add a new recipe, drop a JSON file in the folder and append its ID to `index.json`.

## Packaging

```bash
./mvnw package
# Run
java -jar target/quarkus-app/quarkus-run.jar

# Or build an über-jar
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

## Native executable

```bash
./mvnw package -Dnative
# Or using a container build
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Run the native binary from `target/recipe-calculator-1.0.0-SNAPSHOT-runner`.

## Branch and PR workflow

The repository work is expected to start from the latest `main` branch. To refresh your checkout and open a new PR based on
`main`:

```bash
git checkout main
git pull origin main   # update your local main
git checkout -b feature/your-change
# make your edits and commits
git push origin feature/your-change
```

Open the PR from your feature branch back into `main`. This keeps feature work cleanly rebased on the up-to-date source.
