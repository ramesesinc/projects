package test.entity;

import com.rameses.gov.etracs.rptis.models.Entity;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntityTest  {
    @BeforeClass
    public static void setUpClass() {
        createDummyEntity();
    }
    
    @Test
    public void shouldCreateEntity() {
        Map entity = Entity.findEntity(Entity.ENTITY_001);
        assertNotNull(entity);
        entity =  Entity.findEntity(Entity.ENTITY_002);
        assertNotNull(entity);
    }

    private static void createDummyEntity() {
        if (Entity.existEntity(Entity.ENTITY_001)){
            return;
        }
        Entity.createEntity(Entity.ENTITY_001);
        Entity.createEntity(Entity.ENTITY_002);
    }
}
