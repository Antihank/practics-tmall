package com.antihank.tmall.search.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Antihank on 2017/5/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/tmall-search-web-servlet.xml")
public class ChangeImageURLTest {
   /* @Autowired
    private ItemService itemService;

    @Test
    public void changeTest() throws Exception {

        //穷举分页获取所有item
        int page = 1;
        int rows = 500;

        do {
            //分页装入
            List<Item> list = itemService.selectAllAsList(page, rows);

            for (int i = 0; i < list.size(); i++) {
                Item item = list.get(i);
                String image = item.getImage();
                String replace = image.replace("http://image.taotao.com/", "http://192.168.12.168/");
                item.setImage(replace);
                itemService.updateByPrimaryKeySelective(item);
            }

            page++;
            rows = list.size();
            System.out.println("已修改第" + page + "页，修改了" + rows + "条。");
        } while (rows == 500);

    }*/
}
