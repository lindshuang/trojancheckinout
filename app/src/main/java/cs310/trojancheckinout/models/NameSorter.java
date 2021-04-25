package cs310.trojancheckinout.models;

import java.util.Comparator;

public class NameSorter implements Comparator<SearchUser>
{
    @Override
    public int compare(SearchUser o1, SearchUser o2) {
        return o1.getLastFirstName().compareToIgnoreCase(o2.getLastFirstName());
    }
}
