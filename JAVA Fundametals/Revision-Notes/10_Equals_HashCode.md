# Overriding equals() without hashCode()

Overriding `equals()` without also overriding `hashCode()` breaks the contract that two equal objects must have the same hash code. This doesn't cause a compile error, but it silently corrupts behavior in any hash-based collection — `HashMap`, `HashSet`, `Hashtable`.

Those collections use `hashCode()` first to decide which "bucket" an object belongs in, and only use `equals()` to compare objects within the same bucket. If two objects are `equals()` but have different `hashCode()` values, they can land in different buckets — so the collection never even checks `equals()` between them. It treats them as distinct, even though `equals()` says they're the same.

## Example

```java
public class Point {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    // hashCode() NOT overridden — uses Object's default (identity-based)
}
```

```java
Point p1 = new Point(1, 2);
Point p2 = new Point(1, 2);

System.out.println(p1.equals(p2)); // true — equals() says they're the same

Set<Point> set = new HashSet<>();
set.add(p1);
System.out.println(set.contains(p2)); // false!
```

`p1.equals(p2)` is `true`, but `p1.hashCode()` and `p2.hashCode()` are different (default `Object.hashCode()` is identity-based). So `HashSet` computes a different bucket for `p2` than the one `p1` lives in, never compares them, and reports `contains` as `false` — even though logically they should be duplicates.

## Fix

Override both consistently, using the same fields:

```java
@Override
public int hashCode() {
    return Objects.hash(x, y);
}
```

## Rule to remember

Equal objects must have equal hash codes — but not vice versa. Unequal objects can share a hash code; that's just a collision, not a contract violation.

# Overriding hashCode() without equals()

This is the reverse mistake, and it's just as broken — but the failure mode is different. Two objects with the same field values will land in the same bucket (since `hashCode()` matches), but the collection still falls back on `Object`'s default `equals()` (identity comparison) to check if they're "the same" within that bucket. Since they're different object instances, `equals()` returns `false`, so the collection treats them as distinct even though your `hashCode()` implies they should be considered equal.

## Example

```java
public class Point {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    // equals() NOT overridden — uses Object's default (identity-based)
}
```

```java
Point p1 = new Point(1, 2);
Point p2 = new Point(1, 2);

System.out.println(p1.hashCode() == p2.hashCode()); // true — same computed hash

Set<Point> set = new HashSet<>();
set.add(p1);
set.add(p2);
System.out.println(set.size()); // 2, not 1!
System.out.println(set.contains(p2)); // true, by luck — p2 itself was added
```

Both objects hash into the same bucket, but since `equals()` still uses identity, `HashSet` sees them as two different elements and stores both — defeating the purpose of using the set to eliminate duplicates. If you'd only added `p1` and checked `set.contains(new Point(1, 2))`, it would return `false`, because the bucket lookup succeeds but the identity-based `equals()` check inside the bucket fails.

## Fix

Override both together, consistently, using the same fields — never one without the other.
