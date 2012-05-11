package jollobajano.pm.data;

import org.junit.Test;

import jollobajano.pm.model.HibernateUtil;
import junit.framework.TestCase;


public class HibernateUtilTest extends TestCase
{
	@Test
	public void testLoad() throws Exception
	{
		HibernateUtil.getSessionFactory().openSession().close();
	}
}
