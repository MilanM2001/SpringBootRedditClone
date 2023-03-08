export class Rule {
    ruleId: number = 0;
    name: string = "";
    description: string = "";

    Rule(ruleId: number, name: string, description: string) {
        this.ruleId = ruleId;
        this.name = name;
        this.description = description;
    }
}