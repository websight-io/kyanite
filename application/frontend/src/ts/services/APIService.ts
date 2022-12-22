export default class APIService {
    static instance: APIService;
    private contactFormEntity: string | null = null

    static getInstance() {
        if (!APIService.instance) {
            APIService.instance = new APIService();
        }
        return APIService.instance;
    }

    private setContactFormEntity(entityName: string) {
        this.contactFormEntity = entityName;
    }
    private getContactFormEntity(): string | boolean {
        if (this.contactFormEntity !== null) {
            return this.contactFormEntity
        } else {
            return false
        }
    }
}