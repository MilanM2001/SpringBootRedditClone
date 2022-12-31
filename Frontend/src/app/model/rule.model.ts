export class Rule {
    rule_id: number = 0;
    description: string = '';

    Rule(rule_id: number, description: string) {
        this.rule_id = rule_id;
        this.description = description;
    }
}