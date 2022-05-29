# Edge
An open-sourced **unfinished** Tablist API.
(**Unfinished** means that the API is not complete, and may return exceptions and/or change in the future)

***

### How to use & implement
```java
/**
 * Implement the TabAdapter interface
 */
public class TabLayout implements TabAdapter {

    /**
     * Return your desired tab header
     */
    @Override
    public String getHeader(Player player) {
        return CC.BLUE + CC.BOLD + "Edge";
    }

    /**
     * Return your desired tab footer
     */
    @Override
    public String getFooter(Player player) {
        return CC.GRAY + CC.ITALIC + "lucanius.me";
    }

    /**
     * Return your desired tab entries by creating a Set of TabEntry
     *
     * new TabEntry(String line, int slot, int latency, Skin skin)
     */
    @Override
    public Set<TabEntry> getEntries(Player player) {
        Set<TabEntry> entries = new HashSet<>();

        for (int i = 0; i < 80; i++) {
            entries.add(new TabEntry(CC.AQUA + "Slot: " + i, i, -1, Skin.EMPTY));
        }

        return entries;
    }
}
```
```java
/**
 * new Edge(Plugin plugin, TabAdapter adapter)
 */
@Override
public void onEnable() {
    new Edge(this, new TabLayout());
}
```

### Contact
You can contact me on discord via my tag or server:
* My discord - lucA#0999
* My server - https://discord.biove.dev/

### Selling & Using
You're free to use this for anything, including using it for your own projects. However, if you want to sell it, leave credits and a link to this repository.