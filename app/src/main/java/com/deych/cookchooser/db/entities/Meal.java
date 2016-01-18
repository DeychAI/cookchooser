package com.deych.cookchooser.db.entities;

/**
 * Created by deigo on 20.12.2015.
 */
public class Meal {

    private String uuid;
    private String name;
    private long categoryId;
    private String group;
    private MealColor color;
    private long revision;
    private boolean changed;
    private boolean deleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public MealColor getColor() {
        return color;
    }

    public void setColor(MealColor color) {
        this.color = color;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Meal meal = (Meal) o;

        return uuid.equals(meal.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
