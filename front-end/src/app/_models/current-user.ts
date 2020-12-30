import { Role } from "./role";

export class CurrentUser {
    id: number;
    username: string;
    email: string;
    roles: Role[];
    accessToken?: string;
    typeToken: string;
}