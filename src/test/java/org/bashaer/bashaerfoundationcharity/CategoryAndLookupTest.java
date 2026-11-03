package org.bashaer.bashaerfoundationcharity;

import org.bashaer.bashaerfoundationcharity.dao.LookupDao;
import org.bashaer.bashaerfoundationcharity.model.CategoryItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryAndLookupTest {

    @Test
    public void testDefaultCategoriesSeeded() {
        List<CategoryItem> categories = LookupDao.getAllCategories();

        boolean foundAnyAll = false;
        boolean foundOther = false;

        for (CategoryItem item : categories) {
            if ("Any/All".equals(item.getName())) {
                foundAnyAll = true;
            }
            if ("Other".equals(item.getName())) {
                foundOther = true;
            }
        }

        assertTrue(foundAnyAll, "Database must contain default 'Any/All' category.");
        assertTrue(foundOther, "Database must contain default 'Other' category.");
    }
}