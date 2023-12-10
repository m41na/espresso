package works.hop.presso.json.naming;

public interface NamingStrategy {

    String resolve(String name);

    String inverse(String name);
}
