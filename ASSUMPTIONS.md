
**ASSUMPTIONS.md**
```markdown
# Assumptions
1. **Customer Types:** Assumed `STANDARD` and `PREMIUM` are the only customer types.
2. **Discount Logic:** Assumed the 10% discount for PREMIUM customers means they pay 90% of the original amount (multiplied by 0.90).
3. **Date Format:** Assumed the `month` query parameter will strictly follow the ISO-8601 `YYYY-MM` format.
4. **In-Memory Storage:** Assumed that data persistence across application restarts is not required for this specific task, hence the use of an in-memory `ConcurrentHashMap`.
5. **OSGi Environment:** Assumed the OSGi bonus is evaluated on code structure and understanding of the OSGi lifecycle/annotations, rather than a fully compiled multi-bundle Maven project, as setting up the latter requires specific IDE plugins (like Bndtools) outside the scope of a standard Spring Boot app.