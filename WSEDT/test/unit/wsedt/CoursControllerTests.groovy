package wsedt



import org.junit.*
import grails.test.mixin.*

@TestFor(CoursController)
@Mock(Cours)
class CoursControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/cours/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.coursInstanceList.size() == 0
        assert model.coursInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.coursInstance != null
    }

    void testSave() {
        controller.save()

        assert model.coursInstance != null
        assert view == '/cours/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/cours/show/1'
        assert controller.flash.message != null
        assert Cours.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/cours/list'


        populateValidParams(params)
        def cours = new Cours(params)

        assert cours.save() != null

        params.id = cours.id

        def model = controller.show()

        assert model.coursInstance == cours
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/cours/list'


        populateValidParams(params)
        def cours = new Cours(params)

        assert cours.save() != null

        params.id = cours.id

        def model = controller.edit()

        assert model.coursInstance == cours
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/cours/list'

        response.reset()


        populateValidParams(params)
        def cours = new Cours(params)

        assert cours.save() != null

        // test invalid parameters in update
        params.id = cours.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/cours/edit"
        assert model.coursInstance != null

        cours.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/cours/show/$cours.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        cours.clearErrors()

        populateValidParams(params)
        params.id = cours.id
        params.version = -1
        controller.update()

        assert view == "/cours/edit"
        assert model.coursInstance != null
        assert model.coursInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/cours/list'

        response.reset()

        populateValidParams(params)
        def cours = new Cours(params)

        assert cours.save() != null
        assert Cours.count() == 1

        params.id = cours.id

        controller.delete()

        assert Cours.count() == 0
        assert Cours.get(cours.id) == null
        assert response.redirectedUrl == '/cours/list'
    }
}
