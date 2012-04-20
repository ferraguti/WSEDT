package wsedt



import org.junit.*
import grails.test.mixin.*

@TestFor(SalleController)
@Mock(Salle)
class SalleControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/salle/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.salleInstanceList.size() == 0
        assert model.salleInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.salleInstance != null
    }

    void testSave() {
        controller.save()

        assert model.salleInstance != null
        assert view == '/salle/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/salle/show/1'
        assert controller.flash.message != null
        assert Salle.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/salle/list'


        populateValidParams(params)
        def salle = new Salle(params)

        assert salle.save() != null

        params.id = salle.id

        def model = controller.show()

        assert model.salleInstance == salle
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/salle/list'


        populateValidParams(params)
        def salle = new Salle(params)

        assert salle.save() != null

        params.id = salle.id

        def model = controller.edit()

        assert model.salleInstance == salle
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/salle/list'

        response.reset()


        populateValidParams(params)
        def salle = new Salle(params)

        assert salle.save() != null

        // test invalid parameters in update
        params.id = salle.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/salle/edit"
        assert model.salleInstance != null

        salle.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/salle/show/$salle.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        salle.clearErrors()

        populateValidParams(params)
        params.id = salle.id
        params.version = -1
        controller.update()

        assert view == "/salle/edit"
        assert model.salleInstance != null
        assert model.salleInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/salle/list'

        response.reset()

        populateValidParams(params)
        def salle = new Salle(params)

        assert salle.save() != null
        assert Salle.count() == 1

        params.id = salle.id

        controller.delete()

        assert Salle.count() == 0
        assert Salle.get(salle.id) == null
        assert response.redirectedUrl == '/salle/list'
    }
}
