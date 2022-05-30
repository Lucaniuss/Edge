# Edge
An open-sourced Tablist API. (This API can produce errors, please report them to me if you encounter any)
***

### How to use & implement
```java
/**
 * Implement the TabAdapter interface
 */
public class TabLayout implements TabAdapter {
    
    private final ExamplePlugin plugin = ExamplePlugin.getInstance();

    /**
     * Return your desired tab header
     */
    @Override
    public List<String> getHeader(Player player) {
        return Arrays.asList(" ", CC.BLUE + CC.BOLD + "Edge", " ");
    }

    /**
     * Return your desired tab footer
     */
    @Override
    public List<String> getFooter(Player player) {
        return Arrays.asList(" ", CC.GRAY + CC.ITALIC + "lucanius.me", " ");
    }

    /**
     * Return your desired tab entries by creating a Set of TabData
     *
     * new TabData(TabColumn column, int slot, String text, int latency, Skin skin)
     */
    @Override
    public Set<TabData> getEntries(Player player) {
        Set<TabData> entries = new HashSet<>();

        entries.add(new TabData(TabColumn.MIDDLE, 1, CC.BLUE + CC.BOLD + "Edge"));
        entries.add(new TabData(TabColumn.MIDDLE, 2, CC.GRAY + CC.ITALIC + "lucanius.me"));

        entries.add(new TabData(TabColumn.LEFT, 4, "Left"));
        entries.add(new TabData(TabColumn.MIDDLE, 4, "Center"));
        entries.add(new TabData(TabColumn.RIGHT, 4, "Right"));
        entries.add(new TabData(TabColumn.FAR_RIGHT, 4, "Far Right"));

        entries.add(new TabData(TabColumn.MIDDLE, 19, "Your Skin", plugin.getEdge().getSkin(player.getUniqueId())));

        return entries;
    }
}
```
```java
private Edge edge;

/**
 * new Edge(Plugin plugin, TabAdapter** **adapter)
 */
@Override
public void onEnable() {
    this.edge = new Edge(this, new TabLayout());
}

@Override
public void onDisable() {
    this.edge.destroy();
}

public Edge getEdge() {
    return this.edge;
}
```

### Contact
You can contact me on discord via my tag or server:
* My discord - lucA#0999
* My server - https://discord.biove.dev/

### Selling & Using
You're free to use this for anything, including using it for your own projects. However, if you want to sell it, leave credits and a link to this repository.