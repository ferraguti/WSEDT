package wsedt



import org.junit.*
import grails.test.mixin.*

@TestFor(DateController)
@Mock(Date)
class DateControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/date/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.dateInstanceList.size() == 0
        assert model.dateInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.dateInstance != null
    }

    void testSave() {
        controller.save()

        assert model.dateInstance != null
        assert view == '/date/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/date/show/1'
        assert controller.flash.message != null
        assert Date.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/date/list'


        populateValidParams(params)
        def date = new Date(params)

        assert date.save() != null

        params.id = date.id

        def model = controller.show()

        assert model.dateInstance == date
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/date/list'


        populateValidParams(params)
        def date = new Date(params)

        assert date.save() != null

        params.id = date.id

        def model = controller.edit()

        assert model.dateInstance == date
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/date/list'

        response.reset()


        populateValidParams(params)
        def date = new Date(params)

        assert date.save() != null

        // test invalid parameters in update
        params.id = date.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/date/edit"
        assert model.dateInstance != null

        date.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/date/show/$date.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        date.clearErrors()

        populateValidParams(params)
        params.id = date.id
        params.version = -1
        controller.update()

        assert view == "/date/edit"
        assert model.dateInstance != null
        assert model.dateInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/date/list'

        response.reset()

        populateValidParams(params)
        def date = new Date(params)

        assert date.save() != null
        assert Date.count() == 1

        params.id = date.id

        controller.delete()

        assert Date.count() == 0
        assert Date.get(date.id) == null
        assert response.redirectedUrl == '/date/list'
    }
}
